package com.sseudam.user.component

import com.sseudam.user.device.UserDevice
import com.sseudam.user.repository.UserDeviceRepository
import org.springframework.stereotype.Component

@Component
class UserDeviceReader(
    private val userDeviceRepository: UserDeviceRepository,
) {
    fun readLastByUserId(userId: Long): UserDevice.Info? = userDeviceRepository.findLastByUserId(userId)

    fun readAllByUserKey(userKey: String): List<UserDevice.Info> = userDeviceRepository.findAllByUserKey(userKey)

    fun readAllByUserId(userId: Long): List<UserDevice.Info> = userDeviceRepository.findAllByUserId(userId)

    fun readAll(): List<UserDevice.Info> = userDeviceRepository.findAll()
}
