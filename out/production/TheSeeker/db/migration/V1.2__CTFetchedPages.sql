CREATE SEQUENCE fetchedpages_seq;

CREATE TABLE FetchedPages(
  id              BIGINT PRIMARY KEY,
  ip              VARCHAR(30) NOT NULL,
  dominio         TEXT,
  titulo          TEXT,
  conteudo        TEXT
);