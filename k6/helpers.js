/**
 * Utilitários compartilhados para os testes de carga.
 *
 * Gerador de CPF com dígitos verificadores válidos,
 * garantindo que o fallback local (CpfValidator) aprove o CPF.
 */

/**
 * Gera um CPF válido (com dígitos verificadores corretos) baseado
 * no VU e na iteração, garantindo unicidade durante o teste.
 *
 * @param {number} vu    - __VU  do k6
 * @param {number} iter  - __ITER do k6
 * @returns {string} CPF de 11 dígitos sem pontuação
 */
export function generateCpf(vu, iter) {
    // 7 dígitos derivados de vu+iter para garantir unicidade
    const seed = (vu * 100000 + iter) % 9999999;
    const base = String(seed).padStart(7, '0');
    // 2 dígitos extras fixos para completar os 9 base digits
    const digits = (base + '00').split('').map(Number);

    // Primeiro dígito verificador
    let sum = 0;
    for (let i = 0; i < 9; i++) sum += digits[i] * (10 - i);
    let d1 = 11 - (sum % 11);
    if (d1 >= 10) d1 = 0;
    digits[9] = d1;

    // Segundo dígito verificador
    sum = 0;
    for (let i = 0; i < 10; i++) sum += digits[i] * (11 - i);
    let d2 = 11 - (sum % 11);
    if (d2 >= 10) d2 = 0;
    digits[10] = d2;

    return digits.join('');
}

export const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

export const JSON_HEADERS = {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
};

/**
 * Cria uma pauta e retorna o ID.
 * @param {object} http - módulo http do k6
 * @param {string} suffix - sufixo para tornar o título único
 * @returns {string|null} UUID da pauta criada ou null em caso de erro
 */
export function criarPauta(http, suffix) {
    const res = http.post(
        `${BASE_URL}/api/v1/pautas`,
        JSON.stringify({ titulo: `Pauta Load Test ${suffix}`, descricao: 'Criada via teste de carga' }),
        { headers: JSON_HEADERS }
    );
    if (res.status !== 201) return null;
    return res.json('id');
}

/**
 * Abre uma sessão para a pauta e retorna o ID da sessão.
 * @param {object} http - módulo http do k6
 * @param {string} pautaId - UUID da pauta
 * @param {number} duracaoMinutos - duração da sessão em minutos
 * @returns {string|null}
 */
export function abrirSessao(http, pautaId, duracaoMinutos) {
    const res = http.post(
        `${BASE_URL}/api/v1/pautas/${pautaId}/sessoes`,
        JSON.stringify({ duracaoMinutos }),
        { headers: JSON_HEADERS }
    );
    if (res.status !== 201) return null;
    return res.json('id');
}
