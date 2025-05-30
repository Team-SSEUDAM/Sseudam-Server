package com.sseudam.storage.db.core.trashspot

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.springframework.data.jpa.repository.JpaRepository

interface TrashSpotJpaRepository :
    JpaRepository<TrashSpotEntity, Long>,
    KotlinJdslJpqlExecutor
