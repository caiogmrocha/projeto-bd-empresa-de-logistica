-- From "Cliente" relational database specification
CREATE TABLE customers (
  id SERIAL PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  addresses_id BIGINT UNSIGNED NOT NULL,
  credit_limit DECIMAL(10,2) UNSIGNED NOT NULL DEFAULT 0.00 ,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
  deleted_at TIMESTAMP NULL,

  CONSTRAINT fk__customers__addresses_id__addresses__id
    FOREIGN KEY (addresses_id) REFERENCES addresses(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
