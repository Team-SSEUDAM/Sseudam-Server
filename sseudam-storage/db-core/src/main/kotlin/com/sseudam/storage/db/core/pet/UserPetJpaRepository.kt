package com.sseudam.storage.db.core.pet

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.springframework.data.jpa.repository.JpaRepository

interface UserPetJpaRepository :
    JpaRepository<UserPetEntity, Long>,
    KotlinJdslJpqlExecutor {
    fun findByUserIdAndDeletedAtIsNull(userId: Long): UserPetEntity?
}
