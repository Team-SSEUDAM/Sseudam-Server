package com.sseudam.admin.infrastructure

import org.springframework.data.jpa.repository.JpaRepository

interface AdminJpaRepository : JpaRepository<AdminEntity, Long> {
    fun findByLoginIdAndPassword(
        loginId: String,
        password: String,
    ): AdminEntity?

    fun findByLoginId(loginId: String): AdminEntity?
}
