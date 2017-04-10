CREATE SEQUENCE queuedURL_seq;

CREATE TABLE queuedURL(
  id              BIGINT PRIMARY KEY,
  dominio         TEXT,
  ip              VARCHAR(30) NOT NULL
);