package com.sseudam.notification

interface NotificationStoredRepository {
    fun save(notificationStored: NotificationStored.Create): NotificationStored.Info

    fun findById(notificationStoredId: Long): NotificationStored.Info

    fun findByUserIdAndReadStatus(
        userId: Long,
        readStatus: ReadStatus,
    ): List<NotificationStored.Info>

    fun countByUserIdAndReadStatus(
        userId: Long,
        readStatus: ReadStatus?,
    ): Long

    fun read(notificationStoredId: Long)
}
