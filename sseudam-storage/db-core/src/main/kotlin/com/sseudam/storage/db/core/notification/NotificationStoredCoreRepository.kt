package com.sseudam.storage.db.core.notification

import com.sseudam.notification.NotificationStored
import com.sseudam.notification.ReadStatus
import com.sseudam.notification.repository.NotificationStoredRepository
import com.sseudam.storage.db.core.support.findByIdOrElseThrow
import com.sseudam.support.cursor.Cursor
import com.sseudam.support.cursor.CursorRequest
import com.sseudam.support.tx.Tx
import org.springframework.stereotype.Repository

@Repository
class NotificationStoredCoreRepository(
    private val notificationStoredJpaRepository: NotificationStoredJpaRepository,
    private val notificationStoredCustomRepository: NotificationStoredCustomRepository,
) : NotificationStoredRepository {
    override fun save(notificationStored: NotificationStored.Create): NotificationStored.Info =
        Tx.writeable {
            notificationStoredJpaRepository
                .save(
                    NotificationStoredEntity(notificationStored),
                ).toNotificationStoredInfo()
        }

    override fun saveAll(createAll: List<NotificationStored.Create>) =
        Tx.writeable {
            notificationStoredJpaRepository
                .saveAll(
                    createAll.map { NotificationStoredEntity(it) },
                ).map { it.toNotificationStoredInfo() }
        }

    override fun findById(notificationStoredId: Long): NotificationStored.Info =
        Tx.readable {
            notificationStoredJpaRepository
                .findByIdOrElseThrow(notificationStoredId)
                .toNotificationStoredInfo()
        }

    override fun findByUserIdAndReadStatus(
        userId: Long,
        readStatus: ReadStatus,
    ): List<NotificationStored.Info> =
        Tx.readable {
            notificationStoredJpaRepository
                .findByUserIdAndReadStatus(userId, readStatus)
                .map { it.toNotificationStoredInfo() }
        }

    override fun findAllBy(
        userId: Long,
        cursorRequest: CursorRequest,
    ): Cursor<NotificationStored.Info> =
        Tx.readable {
            notificationStoredCustomRepository.findAllBy(userId, cursorRequest)
        }

    override fun countByUserIdAndReadStatus(
        userId: Long,
        readStatus: ReadStatus?,
    ): Long =
        Tx.readable {
            notificationStoredJpaRepository
                .countByUserIdAndReadStatus(userId, readStatus)
        }

    override fun markAsRead(notificationId: Long) =
        Tx.writeable {
            val notificationStored =
                notificationStoredJpaRepository
                    .findByIdOrElseThrow(notificationId)

            if (notificationStored.readStatus == ReadStatus.READ) {
                return@writeable
            }

            notificationStored.read()
        }
}
