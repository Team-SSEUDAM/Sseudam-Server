package com.sseudam.storage.db.core.version.app

import com.sseudam.common.DeviceType
import org.springframework.data.jpa.repository.JpaRepository

interface AppVersionJpaRepository : JpaRepository<AppVersionEntity, Long> {
    fun findByDeviceType(deviceType: DeviceType): AppVersionEntity?
}
