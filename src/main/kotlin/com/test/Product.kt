package com.test

data class Product(
    val id: Long,
    var title: String,
    var vendor: String,
    var price: Double = 0.0,
    var productType: String,
    var imageUrl: String?
)
