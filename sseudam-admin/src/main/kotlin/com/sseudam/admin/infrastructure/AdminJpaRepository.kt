package com.sseudam.admin.infrastructure

import org.springframework.data.jpa.repository.JpaRepository

interface AdminJpaRepository : JpaRepository<AdminEntity, Long>
