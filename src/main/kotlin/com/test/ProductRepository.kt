// src/main/kotlin/com/example/demo/repository/ProductRepository.kt
package com.test

import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository

@Repository
class ProductRepository(private val jdbcClient: JdbcClient) {

    private val productRowMapper = ProductRowMapper()


    fun findAll(): List<Product> {
        val sql = "SELECT * FROM products"
        return jdbcClient.sql(sql).query(productRowMapper).list()
    }

    fun save(product: Product) {
        jdbcClient.sql("INSERT INTO products (id, title, vendor, price, product_type, image_url) VALUES (?, ?, ?, ?, ?, ?)")
            .param(product.id)
            .param(product.title)
            .param(product.vendor)
            .param(product.price)
            .param(product.productType)
            .param(product.imageUrl)
            .update()
    }

    fun findById(id: Long): Product? = jdbcClient.sql("SELECT * FROM products WHERE id = ?").param(id).query(Product::class.java).optional().orElse(null)

    fun update(product: Product) {
        jdbcClient.sql("UPDATE products SET title = ?, vendor = ?, price = ?, product_type = ?, image_url = ? WHERE id = ?")
            .param(product.title)
            .param(product.vendor)
            .param(product.price)
            .param(product.productType)
            .param(product.imageUrl)
            .param(product.id)
            .update()
    }

    fun deleteById(id: Long): Int = jdbcClient.sql("DELETE FROM products WHERE id = ?").param(id).update()

    fun findByTitleContaining(title: String): List<Product> = jdbcClient.sql("SELECT * FROM products WHERE title ILIKE ?")
        .param("%$title%")
        .query(Product::class.java)
        .list()

    fun existsById(id: Long): Boolean {
        val count = jdbcClient.sql("SELECT COUNT(*) FROM products WHERE id = ?")
            .param(id)
            .query(Int::class.java)
            .optional()
            .orElse(0)
        return count > 0
    }

}