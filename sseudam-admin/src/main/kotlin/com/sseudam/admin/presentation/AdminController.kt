package com.sseudam.admin.presentation

import com.sseudam.admin.application.AdminFacade
import com.sseudam.admin.presentation.request.AdminLoginRequest
import com.sseudam.admin.presentation.response.AdminTokenResponse
import com.sseudam.admin.presentation.response.user.UserAllResponse
import com.sseudam.admin.presentation.response.user.UserResponse
import com.sseudam.support.cursor.OffsetPageRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "🔐 Admin API", description = "관리자 관련 API 입니다.")
@RestController
@RequestMapping("/api/v1/admin")
class AdminController(
    private val adminFacade: AdminFacade,
) {
    @Operation(summary = "어드민 로그인", description = "어드민 로그인을 합니다.")
    @PostMapping("/login")
    fun login(
        @RequestBody request: AdminLoginRequest,
    ): AdminTokenResponse {
        val token = adminFacade.login(request.loginId, request.password)
        return AdminTokenResponse.of(token)
    }

    @Operation(summary = "사용자 리스트 조회", description = "사용자 리스트를 조회합니다.")
    @GetMapping("/users")
    fun findUsers(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
    ): UserAllResponse =
        UserAllResponse.of(
            adminFacade.findUsers(OffsetPageRequest(page, size)).map { UserResponse.of(it) },
        )
}
