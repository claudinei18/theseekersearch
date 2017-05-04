CREATE SEQUENCE queuedURL_seq;

CREATE TABLE queuedURL(
  id              BIGINT,
  dominio         TEXT PRIMARY KEY,
  ip              VARCHAR(30) NOT NULL
);