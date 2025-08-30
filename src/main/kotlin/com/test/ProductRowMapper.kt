// src/main/kotlin/com/test/ProductRowMapper.kt
package com.test

import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class ProductRowMapper : RowMapper<Product> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Product? {
        // You must manually map each column to the corresponding data class property.
        return Product(
            id = rs.getLong("id"),
            title = rs.getString("title"),
            vendor = rs.getString("vendor"),
            price = rs.getDouble("price"),
            productType = rs.getString("product_type"),
            imageUrl = rs.getString("image_url")
        )
    }
}