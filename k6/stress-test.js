/**
 * Stress Test — Votacao API
 *
 * Aumenta progressivamente a carga até encontrar o ponto de ruptura,
 * depois verifica se a aplicação se recupera após reduzir os VUs.
 *
 * Execução:
 *   k6 run k6/stress-test.js
 *   k6 run k6/stress-test.js -e BASE_URL=http://meu-servidor:8080
 *
 * Interpretação dos resultados:
 *   - O ponto em que p(99) > 3s ou error_rate > 5% indica saturação
 *   - A fase de recuperação (última rampa descendente) valida resiliência
 */
import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate, Trend } from 'k6/metrics';
import { BASE_URL, JSON_HEADERS, generateCpf } from './helpers.js';

const errorRate   = new Rate('errors');
const votoLatency = new Trend('voto_latency_ms', true);

let pautasDisponiveis = [];

export const options = {
    scenarios: {
        stress: {
            executor: 'ramping-vus',
            startVUs: 0,
            stages: [
                { duration: '1m',  target: 20  },  // aquecimento
                { duration: '1m',  target: 50  },  // carga normal
                { duration: '1m',  target: 100 },  // alta carga
                { duration: '1m',  target: 150 },  // sobrecarga
                { duration: '1m',  target: 200 },  // stress máximo
                { duration: '30s', target: 0   },  // recuperação
            ],
            gracefulRampDown: '30s',
        },
    },
    thresholds: {
        // Thresholds mais relaxados — objetivo é observar degradação, não falhar o teste
        http_req_duration: ['p(95)<3000'],
        errors: ['rate<0.10'],
    },
};

export function setup() {
    const pautas = [];

    // Criar pautas suficientes para aguentar alta carga
    for (let i = 0; i < 50; i++) {
        const createRes = http.post(
            `${BASE_URL}/api/v1/pautas`,
            JSON.stringify({ titulo: `Stress Test Pauta ${i}`, descricao: 'Stress' }),
            { headers: JSON_HEADERS }
        );
        if (createRes.status !== 201) continue;

        const pautaId = createRes.json('id');
        const sessaoRes = http.post(
            `${BASE_URL}/api/v1/pautas/${pautaId}/sessoes`,
            JSON.stringify({ duracaoMinutos: 15 }),
            { headers: JSON_HEADERS }
        );
        if (sessaoRes.status === 201) {
            pautas.push(pautaId);
        }
    }

    console.log(`Stress setup: ${pautas.length} pautas com sessões abertas`);
    return { pautas };
}

export default function (data) {
    const pautas = data.pautas;
    if (pautas.length === 0) return;

    // Distribui VUs uniformemente pelas pautas disponíveis
    const pautaId = pautas[__VU % pautas.length];
    const cpf = generateCpf(__VU, __ITER);

    // Operação de escrita (mais pesada)
    if (__ITER % 3 !== 2) {
        const start = Date.now();
        const res = http.post(
            `${BASE_URL}/api/v1/pautas/${pautaId}/votes`,
            JSON.stringify({ cpf, option: 'SIM' }),
            { headers: JSON_HEADERS, tags: { name: 'POST_vote_stress' } }
        );
        votoLatency.add(Date.now() - start);

        check(res, {
            'voto aceito ou duplicado': (r) => r.status === 201 || r.status === 409,
        });
        errorRate.add(res.status !== 201 && res.status !== 409);
    } else {
        // Operação de leitura (menor custo)
        const listRes = http.get(
            `${BASE_URL}/api/v1/pautas?page=0&size=20`,
            { headers: JSON_HEADERS, tags: { name: 'GET_pautas_stress' } }
        );
        check(listRes, { 'leitura ok': (r) => r.status === 200 });
        errorRate.add(listRes.status !== 200);
    }

    sleep(0.1); // think time mínimo para simular carga real
}
