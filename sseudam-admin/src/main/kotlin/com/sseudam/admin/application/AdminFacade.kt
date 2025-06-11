package com.sseudam.admin.application

import com.sseudam.admin.domain.AdminToken
import com.sseudam.auth.AuthenticationService
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AdminFacade(
    private val adminService: AdminService,
    private val authService: AuthenticationService,
    private val passwordEncoder: PasswordEncoder,
) {
    fun login(
        loginId: String,
        password: String,
    ): AdminToken {
        val admin = adminService.findAdminUser(loginId)

        if (!passwordEncoder.matches(password, admin.password)) {
            throw ErrorException(ErrorType.INVALID_PASSWORD)
        }

        val token = authService.adminLogin(admin.id)
        return AdminToken(token.accessToken, token.refreshToken)
    }
}
