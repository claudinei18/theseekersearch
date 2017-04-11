CREATE SEQUENCE seenUrl_seq;

CREATE TABLE seenUrl(
  id              BIGINT,
  dominio         TEXT PRIMARY KEY,
  ip              VARCHAR(30) NOT NULL
);