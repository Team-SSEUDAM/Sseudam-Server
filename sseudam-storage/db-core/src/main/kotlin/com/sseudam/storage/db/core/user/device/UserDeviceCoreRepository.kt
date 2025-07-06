package com.sseudam.storage.db.core.user.device

import com.sseudam.support.tx.TxAdvice
import com.sseudam.user.device.UserDevice
import com.sseudam.user.device.UserDeviceRepository
import org.springframework.stereotype.Repository

@Repository
class UserDeviceCoreRepository(
    private val userDeviceJpaRepository: UserDeviceJpaRepository,
    private val txAdvice: TxAdvice,
) : UserDeviceRepository {
    override fun save(create: UserDevice.Create) =
        txAdvice.write {
            userDeviceJpaRepository
                .save(
                    UserDeviceEntity(
                        create,
                    ),
                ).toUserDevice()
        }

    override fun findByUserId(userId: Long): UserDevice.Info? =
        txAdvice.readOnly {
            userDeviceJpaRepository
                .findByUserId(userId)
                .last()
                .toUserDevice()
        }

    override fun findAllByUserKey(userKey: String): List<UserDevice.Info> =
        txAdvice.readOnly {
            userDeviceJpaRepository
                .findAllByUserKey(userKey)
                .map { it.toUserDevice() }
        }

    override fun softDeleteBy(id: Long) {
        txAdvice.write {
            userDeviceJpaRepository.findById(id).ifPresent { entity ->
                entity.softDelete()
                userDeviceJpaRepository.save(entity)
            }
        }
    }
}
