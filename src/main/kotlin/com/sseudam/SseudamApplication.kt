package com.sseudam

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SseudamApplication

fun main(args: Array<String>) {
    runApplication<SseudamApplication>(*args)
}
