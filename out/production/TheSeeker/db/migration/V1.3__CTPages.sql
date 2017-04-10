CREATE SEQUENCE pages_seq;

CREATE TABLE Pages(
  id              BIGINT PRIMARY KEY,
  ip              VARCHAR(30) NOT NULL,
  dominio         TEXT,
  titulo          TEXT,
  conteudo        TEXT
);