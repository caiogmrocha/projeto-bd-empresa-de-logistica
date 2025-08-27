CREATE TABLE products_categories (
  products_id BIGINT UNSIGNED NOT NULL,
  categories_id BIGINT UNSIGNED NOT NULL,

  PRIMARY KEY (products_id, categories_id),

  CONSTRAINT fk__products_categories__products_id__products__id
    FOREIGN KEY (products_id) REFERENCES products(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,

  CONSTRAINT fk__products_categories__categories_id__categories__id
    FOREIGN KEY (categories_id) REFERENCES categories(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
