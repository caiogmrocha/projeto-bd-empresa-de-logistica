CREATE TABLE suppliers (
  id SERIAL PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  addresses_id BIGINT UNSIGNED NULL,
  type ENUM('natural_person', 'legal_entity') NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
  deleted_at TIMESTAMP NULL,

  CONSTRAINT fk__suppliers__addresses_id__addresses__id
    FOREIGN KEY (addresses_id) REFERENCES addresses(id)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
