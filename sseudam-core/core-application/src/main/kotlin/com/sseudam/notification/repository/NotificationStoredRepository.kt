package com.sseudam.notification.repository

import com.sseudam.notification.NotificationStored
import com.sseudam.notification.ReadStatus

interface NotificationStoredRepository {
    fun save(notificationStored: NotificationStored.Create): NotificationStored.Info

    fun saveAll(createAll: List<NotificationStored.Create>): List<NotificationStored.Info>

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
