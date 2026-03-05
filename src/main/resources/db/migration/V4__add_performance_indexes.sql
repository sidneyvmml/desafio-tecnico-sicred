-- Performance indexes for high-volume read operations

-- Pauta: most queries sort/filter by creation date
CREATE INDEX IF NOT EXISTS idx_pauta_criada_em
    ON pauta (criada_em DESC);

-- Sessão: scheduler and vote validation frequently look up open sessions by pauta
CREATE INDEX IF NOT EXISTS idx_sessao_pauta_id
    ON sessao (pauta_id);

-- Sessão: scheduler queries unpublished closed sessions
CREATE INDEX IF NOT EXISTS idx_sessao_unpublished_closed
    ON sessao (resultado_publicado, fechamento)
    WHERE resultado_publicado = false;

-- Voto: support result counting and duplicate-vote lookups by pauta
CREATE INDEX IF NOT EXISTS idx_voto_pauta_cpf
    ON voto (pauta_id, cpf_associado);

CREATE INDEX IF NOT EXISTS idx_voto_pauta_opcao
    ON voto (pauta_id, opcao);
