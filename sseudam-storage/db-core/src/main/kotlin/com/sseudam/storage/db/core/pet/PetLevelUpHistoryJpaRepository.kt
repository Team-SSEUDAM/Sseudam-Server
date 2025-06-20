package com.sseudam.storage.db.core.pet

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Month

interface PetLevelUpHistoryJpaRepository :
    JpaRepository<PetLevelUpHistoryEntity, Long>,
    KotlinJdslJpqlExecutor {
    fun findAllByYearAndMonthlyAndUserPetId(
        year: Int,
        monthly: Month,
        userPetId: Long,
    ): List<PetLevelUpHistoryEntity>
}
