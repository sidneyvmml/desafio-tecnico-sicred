CREATE TABLE IF NOT EXISTS voto (
    id            UUID                    NOT NULL,
    pauta_id      UUID                    NOT NULL,
    cpf_associado CHARACTER VARYING(11)   NOT NULL,
    opcao         CHARACTER VARYING(3)    NOT NULL,
    registrado_em TIMESTAMP               NOT NULL,
    CONSTRAINT pk_voto PRIMARY KEY (id),
    CONSTRAINT uk_voto_pauta_cpf UNIQUE (pauta_id, cpf_associado),
    CONSTRAINT fk_voto_pauta FOREIGN KEY (pauta_id) REFERENCES pauta(id)
);

CREATE INDEX IF NOT EXISTS idx_voto_pauta_id ON voto(pauta_id);
