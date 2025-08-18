package com.sseudam.version.app

import com.sseudam.common.DeviceType
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
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
