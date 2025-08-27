CREATE TABLE products_translations (
  products_id BIGINT UNSIGNED NOT NULL,
  languages_id BIGINT UNSIGNED NOT NULL,
  name VARCHAR(100) NOT NULL,
  description TEXT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
  deleted_at TIMESTAMP NULL,

  PRIMARY KEY (products_id, languages_id),

  CONSTRAINT fk__products_translations__products_id__products__id
    FOREIGN KEY (products_id) REFERENCES products(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,

  CONSTRAINT fk__products_translations__languages_id__languages__id
    FOREIGN KEY (languages_id) REFERENCES languages(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
