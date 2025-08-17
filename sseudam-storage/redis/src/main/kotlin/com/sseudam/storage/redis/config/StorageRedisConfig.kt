package com.sseudam.storage.redis.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory

@Configuration
class StorageRedisConfig(
    private val properties: RedisProperties,
) {
    @Bean
    fun redisConnectionFactory(): LettuceConnectionFactory = LettuceConnectionFactory(properties.host, properties.port)
}
