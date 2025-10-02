package com.sseudam.storage.db.core.user.device

import com.sseudam.support.tx.Tx
import com.sseudam.user.device.UserDevice
import com.sseudam.user.device.UserDeviceRepository
import org.springframework.stereotype.Repository

@Repository
class UserDeviceCoreRepository(
    private val userDeviceJpaRepository: UserDeviceJpaRepository,
) : UserDeviceRepository {
    override fun save(create: UserDevice.Create) =
        Tx.writeable {
            userDeviceJpaRepository
                .save(
                    UserDeviceEntity(
                        create,
                    ),
                ).toUserDevice()
        }

    override fun findAll(): List<UserDevice.Info> =
        Tx.readable {
            userDeviceJpaRepository
                .findAllByDeletedAtIsNull()
                .map { it.toUserDevice() }
        }

    override fun findByUserId(userId: Long): UserDevice.Info? =
        Tx.readable {
            userDeviceJpaRepository
                .findByUserIdAndDeletedAtIsNull(userId)
                .lastOrNull()
                ?.toUserDevice()
        }

    override fun findAllByUserKey(userKey: String): List<UserDevice.Info> =
        Tx.readable {
            userDeviceJpaRepository
                .findAllByUserKey(userKey)
                .map { it.toUserDevice() }
        }

    override fun findAllByUserId(userId: Long): List<UserDevice.Info> =
        Tx.readable {
            userDeviceJpaRepository
                .findByUserIdAndDeletedAtIsNull(userId)
                .map { it.toUserDevice() }
        }

    override fun softDeleteBy(id: Long) {
        Tx.writeable {
            userDeviceJpaRepository.findById(id).ifPresent { entity ->
                entity.softDelete()
                userDeviceJpaRepository.save(entity)
            }
        }
    }
}
