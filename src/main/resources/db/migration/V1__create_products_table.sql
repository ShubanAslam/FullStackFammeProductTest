CREATE TABLE products (
    id BIGINT PRIMARY KEY,
    title VARCHAR(255),
    vendor VARCHAR(255),
    price DECIMAL(10, 2),
    product_type VARCHAR(255),
    image_url VARCHAR(255)
);