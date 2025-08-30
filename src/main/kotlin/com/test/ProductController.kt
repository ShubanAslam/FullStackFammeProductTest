package com.test

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.util.UUID

@Controller
@RequestMapping("/products")
class ProductController(private val productRepository: ProductRepository) {

    @GetMapping
    fun productsPage(model: Model): String {
        model.addAttribute("products", listOf<Product>()) // Initial empty list
        return "products"
    }

    @GetMapping("/list")
    fun listProducts(model: Model): String {
        val products = productRepository.findAll()
        model.addAttribute("products", products)
        return "fragments/product-table :: table"
    }

    @PostMapping
    fun addProduct(@ModelAttribute productDto: ProductDto, model: Model): String {
        val newProduct = Product(
            id = UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE,
            title = productDto.title,
            vendor = productDto.vendor,
            price = productDto.price,
            productType = productDto.productType,
            imageUrl = productDto.imageUrl
        )
        productRepository.save(newProduct)
        model.addAttribute("product", newProduct)
        return "fragments/product-table :: row"
    }

    @GetMapping("/search")
    fun searchProducts(@RequestParam title: String, model: Model): String {
        val products = productRepository.findByTitleContaining(title)
        model.addAttribute("products", products)
        return "fragments/product-table :: table"
    }

    @GetMapping("/edit/{id}")
    fun editProductPage(@PathVariable id: Long, model: Model): String {
        val product = productRepository.findById(id)
        if (product != null) {
            model.addAttribute("product", product)
            return "edit-product"
        }
        return "redirect:/products"
    }

    @PutMapping("/{id}")
    fun updateProduct(@PathVariable id: Long, @ModelAttribute productDto: ProductDto): String {
        val existingProduct = productRepository.findById(id)
        if (existingProduct != null) {
            val updatedProduct = existingProduct.copy(
                title = productDto.title,
                vendor = productDto.vendor,
                price = productDto.price,
                productType = productDto.productType,
                imageUrl = productDto.imageUrl
            )
            productRepository.update(updatedProduct)
            return "fragments/product-table :: row"
        }
        return ""
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: Long): String {
        productRepository.deleteById(id)
        return ""
    }
}