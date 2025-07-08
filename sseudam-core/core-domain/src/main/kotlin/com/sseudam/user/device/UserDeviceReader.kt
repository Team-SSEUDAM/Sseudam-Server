package com.sseudam.user.device

import org.springframework.stereotype.Component

@Component
class UserDeviceReader(
    private val userDeviceRepository: UserDeviceRepository,
) {
    fun readByUserId(userId: Long): UserDevice.Info? = userDeviceRepository.findByUserId(userId)

    fun readAllByUserKey(userKey: String): List<UserDevice.Info> = userDeviceRepository.findAllByUserKey(userKey)

    fun readAllByUserId(userId: Long): List<UserDevice.Info> = userDeviceRepository.findAllByUserId(userId)

    fun readAll(): List<UserDevice.Info> = userDeviceRepository.findAll()
}
