package com.sseudam.config

import com.sseudam.swagger.SwaggerProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.core.GrantedAuthorityDefaults
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.RequestMatcher
import tools.jackson.databind.ObjectMapper

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val swaggerProperties: SwaggerProperties,
    private val objectMapper: ObjectMapper,
) {
    @Bean
    fun grantedAuthorityDefaults(): GrantedAuthorityDefaults = GrantedAuthorityDefaults("")

    @Bean
    @Order(1)
    fun swaggerFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .securityMatcher(getSwaggerUrls())
            .httpBasic { }
            .authorizeHttpRequests { auth ->
                auth.anyRequest().authenticated()
            }

        return http.build()
    }

    fun getSwaggerUrls(): RequestMatcher =
        RequestMatcher { request ->
            val uri = request.requestURI
            uri.startsWith("/swagger-ui/") ||
                uri.startsWith("/v3/api-docs/") ||
                uri.startsWith("/swagger-resources/")
        }

    @Bean
    fun inMemoryUserDetailsManager(): InMemoryUserDetailsManager {
        val user: UserDetails =
            User
                .withUsername(swaggerProperties.user)
                .password(passwordEncoder().encode(swaggerProperties.password))
                .roles("SWAGGER")
                .build()
        return InMemoryUserDetailsManager(user)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        jwtConverter: Converter<Jwt, out AbstractAuthenticationToken>,
    ): SecurityFilterChain {
        http.oauth2ResourceServer {
            it.jwt { jwtConfigurer ->
                jwtConfigurer.jwtAuthenticationConverter(jwtConverter)
            }
        }

        http
            .cors { }
            .headers { it.frameOptions { option -> option.disable() } }
            .csrf { it.disable() }
            .formLogin { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .exceptionHandling { it.authenticationEntryPoint(CustomAuthenticationEntryPoint(objectMapper)) }

        http.httpBasic { it.realmName("Swagger Realm") }

        http.authorizeHttpRequests { authorize ->
            // Swagger 인증
            authorize.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/static/sseudam-api.yaml").permitAll()

            // 인증 없이 허용할 API
            authorize
                .requestMatchers(
                    "/api/v1/auth/**",
                    "/api/v1/auth",
                    "/api/v1/versions/app",
                    "/api/v1/trash-spots/**",
                    "/api/v1/admin/login",
                    "/api/v1/admin/reissue",
                ).permitAll()

            authorize
                .requestMatchers("/api/v1/admin/**")
                .hasRole("ADMIN")

            // 추가로 열어줄 API
            authorize.requestMatchers("/h2-console/**", "/actuator/**", "/ping").permitAll()

            // 그 외 모든 API는 JWT 인증 필요
            authorize
                .requestMatchers(
                    "/api/v1/users/**",
                    "/api/v1/suggestions/**",
                    "/api/v1/reports/**",
                    "/api/v1/visited/**",
                    "/api/v1/pets/**",
                    "/api/v1/attendance/**",
                ).hasRole("USER")

            // 나머지도 다 인증
            authorize.anyRequest().authenticated()
        }

        return http.build()
    }
}
