/**
 * Load Test — Votacao API
 *
 * Simula carga realista com dois perfis de usuário:
 *   - Leitores: consultam pautas e resultados (70% do tráfego)
 *   - Votantes: executam o fluxo completo de votação (30% do tráfego)
 *
 * Execução:
 *   k6 run k6/load-test.js
 *   k6 run k6/load-test.js -e BASE_URL=http://meu-servidor:8080
 *
 * Critérios de sucesso (SLOs):
 *   - p95 latência total < 1s
 *   - p99 latência total < 2s
 *   - Taxa de erro < 1%
 *   - Throughput: mínimo 50 req/s sustentados
 */
import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate, Trend, Counter } from 'k6/metrics';
import { BASE_URL, JSON_HEADERS, generateCpf } from './helpers.js';

// Métricas customizadas
const errorRate      = new Rate('errors');
const votoLatency    = new Trend('voto_latency_ms', true);
const resultLatency  = new Trend('result_latency_ms', true);
const votosRegistrados = new Counter('votos_registrados');

// IDs de pautas criadas no setup, compartilhados por todos os VUs
let pautasDisponiveis = [];

export const options = {
    scenarios: {
        // Rampa gradual: sobe em 1min, sustenta 3min, desce em 1min
        leitores: {
            executor: 'ramping-vus',
            startVUs: 0,
            stages: [
                { duration: '1m', target: 30 },
                { duration: '3m', target: 30 },
                { duration: '1m', target: 0 },
            ],
            gracefulRampDown: '30s',
            exec: 'cenarioLeitura',
        },
        votantes: {
            executor: 'ramping-vus',
            startVUs: 0,
            stages: [
                { duration: '1m', target: 15 },
                { duration: '3m', target: 15 },
                { duration: '1m', target: 0 },
            ],
            gracefulRampDown: '30s',
            exec: 'cenarioVotacao',
        },
    },
    thresholds: {
        http_req_duration:   ['p(95)<1000', 'p(99)<2000'],
        http_req_failed:     ['rate<0.01'],
        errors:              ['rate<0.01'],
        voto_latency_ms:     ['p(95)<800'],
        result_latency_ms:   ['p(95)<500'],
    },
};

/** Cria pautas com sessões abertas que serão usadas pelos VUs de votação. */
export function setup() {
    const pautas = [];
    const qtd = 20; // 20 pautas para distribuir a carga

    for (let i = 0; i < qtd; i++) {
        const createRes = http.post(
            `${BASE_URL}/api/v1/pautas`,
            JSON.stringify({ titulo: `Load Test Pauta ${i}`, descricao: 'Carga' }),
            { headers: JSON_HEADERS }
        );
        if (createRes.status !== 201) {
            console.error(`Falha ao criar pauta ${i}: ${createRes.status} - ${createRes.body}`);
            continue;
        }
        const pautaId = createRes.json('id');

        const sessaoRes = http.post(
            `${BASE_URL}/api/v1/pautas/${pautaId}/sessoes`,
            JSON.stringify({ duracaoMinutos: 10 }),
            { headers: JSON_HEADERS }
        );
        if (sessaoRes.status === 201) {
            pautas.push(pautaId);
        }
    }

    console.log(`Setup concluído: ${pautas.length} pautas com sessões abertas`);
    return { pautas };
}

/** Cenário de leitura: lista pautas paginadas e consulta resultados. */
export function cenarioLeitura(data) {
    const pautas = data.pautas;

    // Listar pautas paginadas (simula navegação)
    const page = Math.floor(Math.random() * 3);
    const listRes = http.get(
        `${BASE_URL}/api/v1/pautas?page=${page}&size=10`,
        { headers: JSON_HEADERS, tags: { name: 'GET_pautas' } }
    );
    check(listRes, {
        'GET /pautas → 200': (r) => r.status === 200,
        'retorna content': (r) => Array.isArray(r.json('content')),
    });
    errorRate.add(listRes.status !== 200);

    sleep(0.5);

    // Buscar pauta específica
    if (pautas.length > 0) {
        const pautaId = pautas[Math.floor(Math.random() * pautas.length)];

        const getRes = http.get(
            `${BASE_URL}/api/v1/pautas/${pautaId}`,
            { headers: JSON_HEADERS, tags: { name: 'GET_pauta_id' } }
        );
        check(getRes, { 'GET /pautas/{id} → 200': (r) => r.status === 200 });
        errorRate.add(getRes.status !== 200);

        sleep(0.3);

        // Consultar resultado
        const start = Date.now();
        const resultRes = http.get(
            `${BASE_URL}/api/v1/pautas/${pautaId}/result`,
            { headers: JSON_HEADERS, tags: { name: 'GET_result' } }
        );
        resultLatency.add(Date.now() - start);
        check(resultRes, { 'GET /result → 200': (r) => r.status === 200 });
        errorRate.add(resultRes.status !== 200);
    }

    sleep(1);
}

/** Cenário de votação: vota em uma pauta aleatória com CPF único. */
export function cenarioVotacao(data) {
    const pautas = data.pautas;
    if (pautas.length === 0) return;

    const pautaId = pautas[Math.floor(Math.random() * pautas.length)];
    const cpf = generateCpf(__VU, __ITER);
    const opcao = Math.random() < 0.6 ? 'SIM' : 'NAO';

    const start = Date.now();
    const res = http.post(
        `${BASE_URL}/api/v1/pautas/${pautaId}/votes`,
        JSON.stringify({ cpf, option: opcao }),
        { headers: JSON_HEADERS, tags: { name: 'POST_vote' } }
    );
    votoLatency.add(Date.now() - start);

    const votoOk = check(res, {
        'POST /votes → 201 ou 409': (r) => r.status === 201 || r.status === 409,
    });

    // 201 = novo voto registrado; 409 = duplicado (esperado em carga)
    if (res.status === 201) {
        votosRegistrados.add(1);
    }
    // Qualquer outro status conta como erro
    errorRate.add(res.status !== 201 && res.status !== 409);

    sleep(0.5);
}
