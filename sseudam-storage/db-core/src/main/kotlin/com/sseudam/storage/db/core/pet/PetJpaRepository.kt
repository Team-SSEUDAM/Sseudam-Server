package com.sseudam.storage.db.core.pet

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Month

interface PetJpaRepository :
    JpaRepository<PetEntity, Long>,
    KotlinJdslJpqlExecutor {
    fun findAllByYearAndMonthly(
        year: Int,
        monthly: Month,
    ): MutableList<PetEntity>
}
