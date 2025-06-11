package com.sseudam.admin.application

import org.springframework.stereotype.Component

@Component
class AdminReader(
    private val adminRepository: AdminRepository,
) {
    fun findAdminLogin(login: String) = adminRepository.findByLogin(login)
}
