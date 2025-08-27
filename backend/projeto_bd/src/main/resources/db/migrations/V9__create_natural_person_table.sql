-- From PESSOA_FISICA relational database specification
CREATE TABLE natural_persons (
  id SERIAL PRIMARY KEY,
  cpf CHAR(11) NOT NULL UNIQUE,
  suppliers_id BIGINT UNSIGNED NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
  deleted_at TIMESTAMP NULL,

  CONSTRAINT fk__natural_persons__suppliers_id__suppliers__id
    FOREIGN KEY (suppliers_id) REFERENCES suppliers(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
