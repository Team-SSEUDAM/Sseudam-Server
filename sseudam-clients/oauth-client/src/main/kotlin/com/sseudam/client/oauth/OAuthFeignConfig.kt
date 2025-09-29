package com.sseudam.client.oauth

import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

@Configuration
@EnableFeignClients(basePackages = ["com.sseudam.client.oauth"])
internal class OAuthFeignConfig
