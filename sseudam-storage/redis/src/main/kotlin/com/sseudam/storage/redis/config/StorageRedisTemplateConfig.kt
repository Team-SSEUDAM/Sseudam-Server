package com.sseudam.storage.redis.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class StorageRedisTemplateConfig {
    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<*, *> =
        RedisTemplate<Any, Any>().apply {
            connectionFactory = redisConnectionFactory
            keySerializer = StringRedisSerializer.UTF_8
            valueSerializer = StringRedisSerializer.UTF_8
        }
}
