package com.sseudam.version.app.component

import com.sseudam.common.DeviceType
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import com.sseudam.version.app.AppVersion
import com.sseudam.version.app.repository.AppVersionRepository
import org.springframework.stereotype.Component

@Component
class AppVersionReader(
    private val appVersionRepository: AppVersionRepository,
) {
    fun readByDeviceType(deviceType: DeviceType): AppVersion =
        appVersionRepository.findByDeviceType(deviceType) ?: throw ErrorException(
            errorType = ErrorType.NOT_FOUND_DATA,
            data = deviceType,
        )
}
