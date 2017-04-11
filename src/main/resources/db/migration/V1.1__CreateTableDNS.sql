CREATE TABLE DNS(
  dominio         TEXT,
  ip              VARCHAR(30) NOT NULL,
  lasttimeaccess  BIGINT,
  robots          boolean
);