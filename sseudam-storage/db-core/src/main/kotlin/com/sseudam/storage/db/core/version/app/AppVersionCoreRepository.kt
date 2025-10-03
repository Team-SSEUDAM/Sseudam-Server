package com.sseudam.storage.db.core.version.app

import com.sseudam.common.DeviceType
import com.sseudam.support.tx.Tx
import com.sseudam.version.app.AppVersion
import com.sseudam.version.app.repository.AppVersionRepository
import org.springframework.stereotype.Repository

@Repository
class AppVersionCoreRepository(
    private val appVersionJpaRepository: AppVersionJpaRepository,
) : AppVersionRepository {
    override fun findByDeviceType(deviceType: DeviceType): AppVersion? =
        Tx.readable {
            appVersionJpaRepository.findByDeviceType(deviceType)?.toAppVersion()
        }
}
