CREATE SEQUENCE dns_seq;

CREATE TABLE DNS(
  id              BIGINT PRIMARY KEY,
  dominio         TEXT,
  ip              VARCHAR(30) NOT NULL
);