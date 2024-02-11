CREATE TABLE clients (
  id SERIAL,
  fio VARCHAR(255) NOT NULL,
  birthday DATE NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  inn VARCHAR(10) UNIQUE NOT NULL,
  PRIMARY KEY(id)
);

CREATE TABLE cards (
  id SERIAL,
  card_number VARCHAR(16) UNIQUE NOT NULL,
  date_granted TIMESTAMP NOT NULL,
  date_expired TIMESTAMP NOT NULL,
  id_client INTEGER NOT NULL,
  PRIMARY KEY(id),
  CONSTRAINT fk_client
        FOREIGN KEY(id_client)
          REFERENCES clients(id)
);
