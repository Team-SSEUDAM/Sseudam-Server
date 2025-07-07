package com.sseudam.user.device

interface UserDeviceRepository {
    fun save(create: UserDevice.Create): UserDevice.Info

    fun findAll(): List<UserDevice.Info>

    fun findByUserId(userId: Long): UserDevice.Info?

    fun findAllByUserKey(userKey: String): List<UserDevice.Info>

    fun findAllByUserId(userId: Long): List<UserDevice.Info>

    fun softDeleteBy(id: Long)
}
