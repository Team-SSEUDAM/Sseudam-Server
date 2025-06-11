package com.sseudam.admin.infrastructure

import com.sseudam.admin.application.AdminRepository
import com.sseudam.admin.domain.Admin
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import com.sseudam.support.tx.TxAdvice
import org.springframework.stereotype.Repository

@Repository
class AdminCoreRepository(
    private val adminJpaRepository: AdminJpaRepository,
    private val txAdvice: TxAdvice,
) : AdminRepository {
    override fun findByLogin(loginId: String): Admin.Info =
        txAdvice.readOnly {
            val adminAuthentication =
                adminJpaRepository.findByLoginId(loginId)
                    ?: throw ErrorException(ErrorType.NOT_FOUND_DATA)
            adminAuthentication.toAdmin()
        }
}
