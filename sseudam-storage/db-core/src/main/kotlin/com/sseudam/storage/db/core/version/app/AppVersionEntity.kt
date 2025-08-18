package com.sseudam.storage.db.core.version.app

import com.sseudam.common.DeviceType
import com.sseudam.storage.db.core.support.BaseEntity
import com.sseudam.version.app.AppVersion
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table

@Entity
@Table(name = "t_app_version")
class AppVersionEntity(
    @Enumerated(value = EnumType.STRING)
    @Column(columnDefinition = "varchar(50)")
    val deviceType: DeviceType,
    val version: String,
) : BaseEntity() {
    fun toAppVersion(): AppVersion =
        AppVersion(
            deviceType = deviceType,
            version = version,
        )
}
