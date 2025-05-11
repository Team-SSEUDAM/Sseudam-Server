package com.sseudam

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class SseudamApplication

fun main(args: Array<String>) {
    runApplication<SseudamApplication>(*args)
}
