package com.sseudam.storage.db.core.user.device

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.springframework.data.jpa.repository.JpaRepository

interface UserDeviceJpaRepository :
    JpaRepository<UserDeviceEntity, Long>,
    KotlinJdslJpqlExecutor {
    fun findByUserId(userId: Long): List<UserDeviceEntity>

    fun findAllByDeletedAtIsNull(): List<UserDeviceEntity>

    fun findAllByUserKeyAndDeletedAtIsNull(userKey: String): List<UserDeviceEntity>

    fun findByUserIdAndDeletedAtIsNull(userId: Long): List<UserDeviceEntity>
}
