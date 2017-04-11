CREATE SEQUENCE rejectedURL_seq;

CREATE TABLE rejectedURL(
  id              BIGINT,
  dominio         TEXT PRIMARY KEY,
  ip              VARCHAR(30) NOT NULL,
  quemRejeitou    TEXT
);