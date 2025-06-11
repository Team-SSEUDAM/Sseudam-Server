package com.sseudam.admin.application

import com.sseudam.admin.domain.Admin
import org.springframework.stereotype.Service

@Service
class AdminService(
    private val adminReader: AdminReader,
) {
    fun findAdminUser(loginId: String): Admin.Info = adminReader.findAdminLogin(loginId)
}
