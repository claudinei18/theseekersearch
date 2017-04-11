CREATE SEQUENCE orderedurl_seq;

CREATE TABLE orderedurl(
  id              BIGINT,
  url             TEXT PRIMARY KEY,
  priority        INTEGER
);