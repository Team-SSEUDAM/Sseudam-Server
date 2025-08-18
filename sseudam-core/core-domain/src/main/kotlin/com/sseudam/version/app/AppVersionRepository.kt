package com.sseudam.version.app

import com.sseudam.common.DeviceType

interface AppVersionRepository {
    fun findByDeviceType(deviceType: DeviceType): AppVersion?
}
