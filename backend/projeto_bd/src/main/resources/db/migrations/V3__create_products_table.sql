CREATE TABLE products (
  id SERIAL PRIMARY KEY,
  warranty_date DATE NOT NULL,
  status ENUM('Tested', 'Returned') NOT NULL,
  minimum_sale_price DECIMAL(10,2) UNSIGNED NOT NULL DEFAULT 0.00,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
  deleted_at TIMESTAMP NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
