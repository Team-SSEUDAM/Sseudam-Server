package com.sseudam.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import java.text.SimpleDateFormat

@Configuration
class JacksonConfig {
    @Bean
    fun objectMapper(): ObjectMapper =
        Jackson2ObjectMapperBuilder()
            .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .dateFormat(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS"))
            .build()
}
