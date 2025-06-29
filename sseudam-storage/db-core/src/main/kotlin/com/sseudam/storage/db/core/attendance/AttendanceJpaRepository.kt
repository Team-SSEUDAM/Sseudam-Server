package com.sseudam.storage.db.core.attendance

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.springframework.data.jpa.repository.JpaRepository

interface AttendanceJpaRepository :
    JpaRepository<AttendanceEntity, Long>,
    KotlinJdslJpqlExecutor {
    fun findFirstByUserIdOrderByDateDesc(userId: Long): AttendanceEntity?
}
