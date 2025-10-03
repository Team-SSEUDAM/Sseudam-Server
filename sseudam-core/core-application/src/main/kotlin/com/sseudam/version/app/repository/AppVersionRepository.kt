package com.sseudam.version.app.repository

import com.sseudam.common.DeviceType
import com.sseudam.version.app.AppVersion

interface AppVersionRepository {
    fun findByDeviceType(deviceType: DeviceType): AppVersion?
}
