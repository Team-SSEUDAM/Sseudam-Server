package com.sseudam.storage.db.core.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement
@EntityScan(
    basePackages = [
        "com.sseudam.storage.db.core",
        "com.sseudam.admin.infrastructure",
    ],
)
@EnableJpaRepositories(
    basePackages = [
        "com.sseudam.storage.db.core",
        "com.sseudam.admin.infrastructure",
    ],
)
class CoreJpaConfig
