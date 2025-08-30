package com.test

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class ProductFetcher(
    private val productRepository: ProductRepository,
    private val webClient: WebClient
) {

    private val logger = LoggerFactory.getLogger(ProductFetcher::class.java)

    @Scheduled(initialDelay = 0, fixedDelay = Long.MAX_VALUE)
    fun fetchAndSaveProducts() {
        logger.info("Running fetchAndSaveProducts...")

        val products = webClient.get().uri("/products.json")
            .retrieve()
            .bodyToMono(Map::class.java)
            .block()?.get("products") as? List<Map<String, Any>> ?: run {
            logger.warn("No products found from API")
            return
        }

        logger.info("Fetched ${products.size} products from API")

        products.take(50).forEach { productJson ->
            val id = (productJson["id"] as? Number)?.toLong() ?: 0L
            val title = productJson["title"] as String
            val vendor = productJson["vendor"] as String
            val productType = productJson["product_type"] as String
            val variants = (productJson["variants"] as List<Map<String, Any>>)
            val price = variants.firstOrNull()?.get("price")?.toString()?.toDoubleOrNull() ?: 0.0
            val featuredImage = variants.firstOrNull()?.get("featured_image") as? Map<String, Any>?
            val imageUrl = featuredImage?.get("src") as String

            val product = Product(id, title, vendor, price, productType, imageUrl)

            try {
                if (productRepository.existsById(id)) {
                    // Update existing product
                    val existingProduct = productRepository.findById(id)
                    existingProduct?.title = title
                    existingProduct?.vendor = vendor
                    existingProduct?.price = price
                    existingProduct?.productType = productType
                    existingProduct?.imageUrl = imageUrl
                    productRepository.update(existingProduct!!)
                    logger.info("Updated product: $title (ID: $id)")
                } else {
                    // Insert new product
                    productRepository.save(product)
                    logger.info("Saved new product: $title (ID: $id)")
                }
            } catch (e: Exception) {
                logger.error("Failed to save or update product: $title (ID: $id)", e)
            }

        }
    }
}
