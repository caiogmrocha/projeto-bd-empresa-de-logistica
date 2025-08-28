-- From "ENTREGA" relational database specification
CREATE TABLE deliveries (
  id SERIAL PRIMARY KEY,
  companies_id BIGINT UNSIGNED NOT NULL,
  orders_id BIGINT UNSIGNED NOT NULL,
  price DECIMAL(10,2) UNSIGNED NOT NULL,
  status VARCHAR(20) NOT NULL,
  addresses_destination_id BIGINT UNSIGNED NOT NULL,
  warehouses_source_id BIGINT UNSIGNED NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
  deleted_at TIMESTAMP NULL,

  CONSTRAINT fk__deliveries__companies_id__companies__id
    FOREIGN KEY (companies_id) REFERENCES companies(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,

  CONSTRAINT fk__deliveries__orders_id__orders__id
    FOREIGN KEY (orders_id) REFERENCES orders(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,

  CONSTRAINT fk__deliveries__addresses_destination_id__addresses__id
    FOREIGN KEY (addresses_destination_id) REFERENCES addresses(id)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,

  CONSTRAINT fk__deliveries__warehouses_source_id__warehouses__id
    FOREIGN KEY (warehouses_source_id) REFERENCES warehouses(id)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
