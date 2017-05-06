CREATE SEQUENCE fetchedpages_seq;

CREATE TABLE FetchedPages(
  id              BIGINT,
  ip              VARCHAR(30) NOT NULL,
  dominio         TEXT PRIMARY KEY,
  titulo          TEXT,
  conteudo        TEXT
);