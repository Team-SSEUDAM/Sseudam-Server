package com.sseudam.storage.db.core.report

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.springframework.data.jpa.repository.JpaRepository

interface SpotReportJpaRepository :
    JpaRepository<SpotReportEntity, Long>,
    KotlinJdslJpqlExecutor
