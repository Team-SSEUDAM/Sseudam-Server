package com.sseudam.version.app

import com.sseudam.common.DeviceType

data class AppVersion(
    val deviceType: DeviceType,
    val currentVersion: String,
    val criticalVersion: String,
)
