package com.sseudam.client.config

import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration
import org.springframework.retry.annotation.EnableRetry

@Configuration
@EnableFeignClients(basePackages = ["com.sseudam.client.discord", "com.sseudam.client.notification"])
@EnableRetry
internal class NotificationFeignConfig
