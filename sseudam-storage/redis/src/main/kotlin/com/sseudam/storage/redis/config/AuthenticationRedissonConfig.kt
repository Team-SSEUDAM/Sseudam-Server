package com.sseudam.storage.redis.config

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AuthenticationRedissonConfig(
    private val authenticationRedisProperties: AuthenticationRedisProperties,
) {
    @Bean
    fun authRedissonClient(): RedissonClient {
        val config = Config()
        config
            .useSingleServer()
            .setAddress("redis://${authenticationRedisProperties.host}:${authenticationRedisProperties.port}")
            .setConnectionMinimumIdleSize(1)
            .setConnectionPoolSize(10)
            .setConnectTimeout(1500)
            .setRetryAttempts(3)
        return Redisson.create(config)
    }
}
