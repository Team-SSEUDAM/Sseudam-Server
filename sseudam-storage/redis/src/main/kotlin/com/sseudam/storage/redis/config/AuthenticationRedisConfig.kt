package com.sseudam.storage.redis.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class AuthenticationRedisConfig(
    private val authenticationRedisProperties: AuthenticationRedisProperties,
) {
    @Bean
    fun authenticationRedisConnectionFactory(): LettuceConnectionFactory =
        LettuceConnectionFactory(RedisStandaloneConfiguration(authenticationRedisProperties.host, authenticationRedisProperties.port))

    @Bean
    fun authenticationRedisTemplate(
        @Qualifier("authenticationRedisConnectionFactory") redisConnectionFactory: RedisConnectionFactory,
    ): RedisTemplate<*, *> =
        RedisTemplate<Any, Any>().apply {
            connectionFactory = redisConnectionFactory
            keySerializer = StringRedisSerializer.UTF_8
            valueSerializer = StringRedisSerializer.UTF_8
        }
}
