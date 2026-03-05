/**
 * Smoke Test — Votacao API
 *
 * Objetivo: verificar que a API está no ar e todos os endpoints
 * respondem corretamente antes de executar os testes de carga.
 *
 * Execução:
 *   k6 run k6/smoke-test.js
 *   k6 run k6/smoke-test.js -e BASE_URL=http://meu-servidor:8080
 *
 * Critérios de sucesso:
 *   - Todas as chamadas HTTP retornam status esperado
 *   - p95 de latência < 500ms
 *   - 0 erros
 */
import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate } from 'k6/metrics';
import { BASE_URL, JSON_HEADERS, generateCpf } from './helpers.js';

const errorRate = new Rate('errors');

export const options = {
    vus: 1,
    iterations: 3,
    thresholds: {
        http_req_duration: ['p(95)<500'],
        errors: ['rate==0'],
    },
};

export default function () {
    const vu = __VU;
    const iter = __ITER;

    // 1. Criar pauta
    const createRes = http.post(
        `${BASE_URL}/api/v1/pautas`,
        JSON.stringify({ titulo: `Smoke Test Pauta ${vu}-${iter}`, descricao: 'Smoke test' }),
        { headers: JSON_HEADERS }
    );
    const pautaOk = check(createRes, {
        'POST /pautas → 201': (r) => r.status === 201,
        'pauta tem id': (r) => r.json('id') !== null,
    });
    errorRate.add(!pautaOk);
    if (!pautaOk) return;

    const pautaId = createRes.json('id');

    // 2. Listar pautas
    const listRes = http.get(`${BASE_URL}/api/v1/pautas?page=0&size=10`, { headers: JSON_HEADERS });
    const listOk = check(listRes, {
        'GET /pautas → 200': (r) => r.status === 200,
        'retorna content': (r) => Array.isArray(r.json('content')),
        'retorna totalElements': (r) => r.json('totalElements') >= 0,
    });
    errorRate.add(!listOk);

    // 3. Buscar pauta por ID
    const getRes = http.get(`${BASE_URL}/api/v1/pautas/${pautaId}`, { headers: JSON_HEADERS });
    const getOk = check(getRes, {
        'GET /pautas/{id} → 200': (r) => r.status === 200,
        'id correto': (r) => r.json('id') === pautaId,
    });
    errorRate.add(!getOk);

    // 4. Abrir sessão (2 minutos para não fechar durante o smoke)
    const sessaoRes = http.post(
        `${BASE_URL}/api/v1/pautas/${pautaId}/sessoes`,
        JSON.stringify({ duracaoMinutos: 2 }),
        { headers: JSON_HEADERS }
    );
    const sessaoOk = check(sessaoRes, {
        'POST /sessoes → 201': (r) => r.status === 201,
        'sessao tem id': (r) => r.json('id') !== null,
    });
    errorRate.add(!sessaoOk);
    if (!sessaoOk) return;

    // 5. Registrar voto
    const cpf = generateCpf(vu, iter);
    const votoRes = http.post(
        `${BASE_URL}/api/v1/pautas/${pautaId}/votes`,
        JSON.stringify({ cpf, option: 'SIM' }),
        { headers: JSON_HEADERS }
    );
    const votoOk = check(votoRes, {
        'POST /votes → 201': (r) => r.status === 201,
    });
    errorRate.add(!votoOk);

    // 6. Consultar resultado
    const resultRes = http.get(`${BASE_URL}/api/v1/pautas/${pautaId}/result`, { headers: JSON_HEADERS });
    const resultOk = check(resultRes, {
        'GET /result → 200': (r) => r.status === 200,
        'resultado tem winner': (r) => r.json('winner') !== null,
        'total >= 1': (r) => r.json('total') >= 1,
    });
    errorRate.add(!resultOk);

    sleep(1);
}
