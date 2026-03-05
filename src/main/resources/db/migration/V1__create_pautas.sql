CREATE TABLE IF NOT EXISTS pauta (
    id          UUID        NOT NULL DEFAULT gen_random_uuid(),
    titulo      VARCHAR(255) NOT NULL,
    descricao   TEXT,
    criada_em   TIMESTAMP   NOT NULL,
    CONSTRAINT pk_pauta PRIMARY KEY (id)
);
