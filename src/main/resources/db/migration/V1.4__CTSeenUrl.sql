CREATE SEQUENCE seenUrl_seq;

CREATE TABLE seenUrl(
  id              BIGINT PRIMARY KEY,
  dominio         TEXT,
  ip              VARCHAR(30) NOT NULL
);