CREATE TABLE customers_emails (
  customers_id BIGINT UNSIGNED PRIMARY KEY,
  email VARCHAR(100) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
  deleted_at TIMESTAMP NULL,

  CONSTRAINT fk__customers_emails__customers_id__customers__id
    FOREIGN KEY (customers_id) REFERENCES customers(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
