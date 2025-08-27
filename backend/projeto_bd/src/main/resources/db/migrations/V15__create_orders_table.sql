-- From "Pedido" relational database specification
CREATE TABLE orders (
  id SERIAL PRIMARY KEY,
  order_method ENUM('online', 'in_person') NOT NULL,
  status ENUM('pending', 'processing', 'shipped', 'delivered', 'canceled') NOT NULL,
  customers_id BIGINT UNSIGNED NOT NULL,
  ordered_at TIMESTAMP NOT NULL DEFAULT NOW(),
  expected_to_deliver_at TIMESTAMP NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
  deleted_at TIMESTAMP NULL,

  CONSTRAINT fk__orders__customers_id__customers__id
    FOREIGN KEY (customers_id) REFERENCES customers(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,

  CONSTRAINT check__orders__expected_to_deliver_at__future_date
    CHECK (expected_to_deliver_at >= ordered_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
