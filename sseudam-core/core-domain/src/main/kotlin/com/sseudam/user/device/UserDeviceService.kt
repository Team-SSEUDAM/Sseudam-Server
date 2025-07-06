package com.sseudam.user.device

import org.springframework.stereotype.Service

@Service
class UserDeviceService(
    private val userDeviceAppender: UserDeviceAppender,
    private val userDeviceRepository: UserDeviceRepository,
) {
    fun append(create: UserDevice.Create) {
        userDeviceAppender.append(create)
    }

    fun findByUserId(userId: Long): UserDevice.Info? = userDeviceRepository.findByUserId(userId)

    fun findAllByUserKey(userKey: String): List<UserDevice.Info> = userDeviceRepository.findAllByUserKey(userKey)
}
