CREATE SEQUENCE pages_seq;

CREATE TABLE Pages(
  id              BIGINT,
  ip              VARCHAR(30) NOT NULL,
  dominio         TEXT PRIMARY KEY,
  titulo          TEXT,
  conteudo        TEXT
);