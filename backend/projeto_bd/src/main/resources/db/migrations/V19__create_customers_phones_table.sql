CREATE TABLE customers_phones (
  customers_id BIGINT UNSIGNED PRIMARY KEY,
  phone VARCHAR(15) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
  deleted_at TIMESTAMP NULL,

  CONSTRAINT fk__customers_phones__customers_id__customers__id
    FOREIGN KEY (customers_id) REFERENCES customers(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
