CREATE TABLE IF NOT EXISTS sessao (
    id                  UUID        NOT NULL,
    pauta_id            UUID        NOT NULL,
    abertura            TIMESTAMP   NOT NULL,
    fechamento          TIMESTAMP   NOT NULL,
    resultado_publicado BOOLEAN     NOT NULL DEFAULT false,
    CONSTRAINT pk_sessao PRIMARY KEY (id),
    CONSTRAINT fk_sessao_pauta FOREIGN KEY (pauta_id) REFERENCES pauta(id)
);

CREATE INDEX IF NOT EXISTS idx_sessao_pauta_id ON sessao(pauta_id);
