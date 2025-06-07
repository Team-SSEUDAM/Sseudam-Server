package com.sseudam.storage.db.core.visit

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.springframework.data.jpa.repository.JpaRepository

interface SpotVisitedJpaRepository :
    JpaRepository<SpotVisitedEntity, Long>,
    KotlinJdslJpqlExecutor {
    fun findAllByUserId(userId: Long): List<SpotVisitedEntity>
}
