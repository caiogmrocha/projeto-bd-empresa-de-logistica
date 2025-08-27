-- From "Estoque" relational database specification
CREATE TABLE product_stocks (
  id SERIAL PRIMARY KEY,
  code VARCHAR(50) NOT NULL UNIQUE,
  amount BIGINT UNSIGNED NOT NULL,
  products_id BIGINT UNSIGNED NOT NULL,
  warehouses_id BIGINT UNSIGNED NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
  deleted_at TIMESTAMP NULL,

  UNIQUE (products_id, warehouses_id),

  CONSTRAINT fk__product_stocks__products_id__products__id
    FOREIGN KEY (products_id) REFERENCES products(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,

  CONSTRAINT fk__product_stocks__warehouses_id__warehouses__id
    FOREIGN KEY (warehouses_id) REFERENCES warehouses(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
