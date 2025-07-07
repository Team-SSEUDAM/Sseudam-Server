package com.sseudam.user.device

import org.springframework.stereotype.Service

@Service
class UserDeviceService(
    private val userDeviceAppender: UserDeviceAppender,
    private val userDeviceReader: UserDeviceReader,
) {
    fun append(create: UserDevice.Create) {
        userDeviceAppender.append(create)
    }

    fun findAll(): List<UserDevice.Info> = userDeviceReader.readAll()

    fun findByUserId(userId: Long): UserDevice.Info? = userDeviceReader.readByUserId(userId)

    fun findAllByUserKey(userKey: String): List<UserDevice.Info> = userDeviceReader.readAllByUserKey(userKey)
}
