package com.sseudam

import io.sentry.spring.boot.jakarta.SentryAutoConfiguration
import org.junit.jupiter.api.Tag
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestConstructor

@Tag("develop")
@SpringBootTest
@EnableAutoConfiguration(
    exclude = [
        SentryAutoConfiguration::class,
    ],
)
@ContextConfiguration(classes = [RedisContainersConfig::class, TestTxConfig::class])
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
annotation class DevelopTest
