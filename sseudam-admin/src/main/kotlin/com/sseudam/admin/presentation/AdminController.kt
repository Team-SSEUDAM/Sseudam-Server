package com.sseudam.admin.presentation

import com.sseudam.admin.presentation.request.AdminLoginRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "🔐 Admin API", description = "관리자 관련 API 입니다.")
@RestController
@RequestMapping("/api/v1/admin")
class AdminController {
    @Operation(summary = "어드민 로그인", description = "어드민 로그인을 합니다.")
    @PostMapping("/login")
    fun login(
        @RequestBody request: AdminLoginRequest,
    ): String = "success"
}
