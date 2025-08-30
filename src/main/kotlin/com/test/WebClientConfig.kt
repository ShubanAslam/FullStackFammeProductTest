// src/main/kotlin/com/test/WebClientConfig.kt
package com.test

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {

    @Bean
    fun webClient(): WebClient {
        val size = 16 * 1024 * 1024 // 16 MB buffer size
        val strategies = ExchangeStrategies.builder()
            .codecs { it.defaultCodecs().maxInMemorySize(size) }
            .build()
        
        return WebClient.builder()
            .baseUrl("https://famme.no")
            .exchangeStrategies(strategies)
            .build()
    }
}