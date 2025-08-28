CREATE TABLE products_suppliers (
  products_id BIGINT UNSIGNED NOT NULL,
  suppliers_id BIGINT UNSIGNED NOT NULL,

  PRIMARY KEY (products_id, suppliers_id),

  CONSTRAINT fk__products_suppliers__products_id__products__id
    FOREIGN KEY (products_id) REFERENCES products(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,

  CONSTRAINT fk__products_suppliers__suppliers_id__suppliers__id
    FOREIGN KEY (suppliers_id) REFERENCES suppliers(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
