package com.sseudam.storage.db.core.pet

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.springframework.data.jpa.repository.JpaRepository

interface PetPointHistoryJpaRepository :
    JpaRepository<PetPointHistoryEntity, Long>,
    KotlinJdslJpqlExecutor
