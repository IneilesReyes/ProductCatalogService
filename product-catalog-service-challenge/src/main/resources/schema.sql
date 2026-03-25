DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS category;

CREATE TABLE IF NOT EXISTS category (
                                        id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                        name VARCHAR(150) NOT NULL UNIQUE
    );

CREATE TABLE IF NOT EXISTS product (
                                       id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                       sku VARCHAR(20) NOT NULL UNIQUE,
    description VARCHAR(500),
    price DECIMAL(10, 2) NOT NULL,
    category_id BIGINT NOT NULL,
    FOREIGN KEY (category_id) REFERENCES category(id)
    );

CREATE INDEX IF NOT EXISTS idx_product_category ON product(category_id);