package com.sseudam.jwt

import com.sseudam.auth.RedisTokenRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.oauth2.jwt.Jwt

@Configuration
class JwtConverterConfig {
    @Bean
    fun jwtConverter(redisTokenRepository: RedisTokenRepository): Converter<Jwt, out AbstractAuthenticationToken> {
        val jwtConverter = JwtConverter(redisTokenRepository)
        jwtConverter.setPrincipalClaimName("jti")
        return jwtConverter
    }
}
