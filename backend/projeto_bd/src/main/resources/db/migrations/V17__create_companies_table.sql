-- From "EMPRESA" relational database specification
CREATE TABLE companies (
  id SERIAL PRIMARY KEY,
  legal_name VARCHAR(100) NOT NULL,
  trade_name VARCHAR(100) NOT NULL,
  cnpj CHAR(14) NOT NULL UNIQUE,
  addresses_id BIGINT UNSIGNED NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
  deleted_at TIMESTAMP NULL,

  CONSTRAINT fk__companies__addresses_id__addresses__id
    FOREIGN KEY (addresses_id) REFERENCES addresses(id)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
