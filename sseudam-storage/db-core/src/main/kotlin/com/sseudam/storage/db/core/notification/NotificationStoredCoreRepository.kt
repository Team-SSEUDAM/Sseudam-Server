package com.sseudam.storage.db.core.notification

import com.sseudam.notification.NotificationStored
import com.sseudam.notification.NotificationStoredRepository
import com.sseudam.notification.ReadStatus
import com.sseudam.storage.db.core.support.findByIdOrElseThrow
import com.sseudam.support.tx.TxAdvice
import org.springframework.stereotype.Repository

@Repository
class NotificationStoredCoreRepository(
    private val notificationStoredJpaRepository: NotificationStoredJpaRepository,
    private val txAdvice: TxAdvice,
) : NotificationStoredRepository {
    override fun save(notificationStored: NotificationStored.Create): NotificationStored.Info =
        txAdvice.write {
            notificationStoredJpaRepository
                .save(
                    NotificationStoredEntity(notificationStored),
                ).toNotificationStoredInfo()
        }

    override fun findById(notificationStoredId: Long): NotificationStored.Info =
        txAdvice.readOnly {
            notificationStoredJpaRepository
                .findByIdOrElseThrow(notificationStoredId)
                .toNotificationStoredInfo()
        }

    override fun findByUserIdAndReadStatus(
        userId: Long,
        readStatus: ReadStatus,
    ): List<NotificationStored.Info> =
        txAdvice.readOnly {
            notificationStoredJpaRepository
                .findByUserIdAndReadStatus(userId, readStatus)
                .map { it.toNotificationStoredInfo() }
        }

    override fun countByUserIdAndReadStatus(
        userId: Long,
        readStatus: ReadStatus?,
    ): Long =
        txAdvice.readOnly {
            notificationStoredJpaRepository
                .countByUserIdAndReadStatus(userId, readStatus)
        }

    override fun read(notificationStoredId: Long) =
        txAdvice.write {
            val notificationStored =
                notificationStoredJpaRepository
                    .findByIdOrElseThrow(notificationStoredId)

            notificationStored.read()
        }
}
