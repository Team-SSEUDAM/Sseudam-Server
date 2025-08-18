package com.sseudam.storage.db.core.version.app

import com.sseudam.common.DeviceType
import com.sseudam.support.tx.TxAdvice
import com.sseudam.version.app.AppVersion
import com.sseudam.version.app.AppVersionRepository
import org.springframework.stereotype.Repository

@Repository
class AppVersionCoreRepository(
    private val appVersionJpaRepository: AppVersionJpaRepository,
    private val txAdvice: TxAdvice,
) : AppVersionRepository {
    override fun findByDeviceType(deviceType: DeviceType): AppVersion? =
        txAdvice.readOnly {
            appVersionJpaRepository.findByDeviceType(deviceType)?.toAppVersion()
        }
}
