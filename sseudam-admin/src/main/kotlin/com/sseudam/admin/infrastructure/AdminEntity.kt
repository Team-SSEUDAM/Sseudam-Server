package com.sseudam.admin.infrastructure

import com.sseudam.admin.domain.Admin
import com.sseudam.storage.db.core.support.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Table(name = "t_admin")
@Entity
class AdminEntity(
    @Column(columnDefinition = "varchar(15) not null")
    val name: String,
    val loginId: String,
    val password: String,
) : BaseEntity() {
    fun toAdmin(): Admin.Info =
        Admin.Info(
            id = id!!,
            name = name,
            loginId = loginId,
            password = password,
            createdAt = createdAt,
        )
}
