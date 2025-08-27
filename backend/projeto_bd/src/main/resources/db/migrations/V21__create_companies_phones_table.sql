CREATE TABLE companies_phones (
  companies_id BIGINT UNSIGNED PRIMARY KEY,
  phone VARCHAR(15) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
  deleted_at TIMESTAMP NULL,

  CONSTRAINT fk__companies_phones__companies_id__companies__id
    FOREIGN KEY (companies_id) REFERENCES companies(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
