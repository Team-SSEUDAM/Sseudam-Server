package com.sseudam.storage.redis.config

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CoreRedissonConfig(
    private val redisProperties: RedisProperties,
) {
    @Bean
    fun coreRedissonClient(): RedissonClient {
        val config = Config()
        config
            .useSingleServer()
            .setAddress("redis://${redisProperties.host}:${redisProperties.port}")
            .setConnectionMinimumIdleSize(1)
            .setConnectionPoolSize(5)
            .setConnectionPoolSize(5)
            .setConnectTimeout(3000)
            .setRetryAttempts(3)
        return Redisson.create(config)
    }
}
