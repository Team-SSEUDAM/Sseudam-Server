package com.sseudam.version.app

import com.sseudam.common.DeviceType
import org.springframework.stereotype.Service

@Service
class AppVersionService(
    private val appVersionReader: AppVersionReader,
) {
    fun getAppVersion(deviceType: DeviceType): AppVersion = appVersionReader.readByDeviceType(deviceType)
}
