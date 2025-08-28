-- From "PEDIDO_PRODUTO" relational database specification
CREATE TABLE orders_products (
  orders_id BIGINT UNSIGNED NOT NULL,
  products_id BIGINT UNSIGNED NOT NULL,
  amount BIGINT UNSIGNED NOT NULL,
  sale_price DECIMAL(10,2) UNSIGNED NOT NULL,

  PRIMARY KEY (orders_id, products_id),

  CONSTRAINT fk__orders_products__orders_id__orders__id
    FOREIGN KEY (orders_id) REFERENCES orders(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,

  CONSTRAINT fk__orders_products__products_id__products__id
    FOREIGN KEY (products_id) REFERENCES products(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DELIMITER $$

CREATE TRIGGER trg_orders_products_price_gte_min_ins
BEFORE INSERT ON orders_products
FOR EACH ROW
BEGIN
  DECLARE minimum_sale_price DECIMAL(10,2);

  SELECT minimum_sale_price INTO minimum_sale_price
  FROM products
  WHERE id = NEW.products_id;

  IF NEW.sale_price < minimum_sale_price THEN
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'sale_price must be >= products.minimum_sale_price';
  END IF;
END$$

CREATE TRIGGER trg_orders_products_price_gte_min_upd
BEFORE UPDATE ON orders_products
FOR EACH ROW
BEGIN
  DECLARE minimum_sale_price DECIMAL(10,2);

  SELECT minimum_sale_price INTO minimum_sale_price
  FROM products
  WHERE id = NEW.products_id;

  IF NEW.sale_price < minimum_sale_price THEN
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'sale_price must be >= products.minimum_sale_price';
  END IF;
END$$

DELIMITER ;
