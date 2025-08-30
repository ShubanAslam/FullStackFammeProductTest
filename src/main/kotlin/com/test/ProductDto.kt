package com.test

data class ProductDto(
    val title: String,
    val vendor: String,
    val price: Double,
    val productType: String,
    val imageUrl: String?
)