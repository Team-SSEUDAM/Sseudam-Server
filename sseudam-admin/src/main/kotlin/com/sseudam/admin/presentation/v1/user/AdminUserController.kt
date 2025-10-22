package com.sseudam.admin.presentation.v1.user

import com.sseudam.admin.application.AdminFacade
import com.sseudam.admin.presentation.response.user.AdminUserResponse
import com.sseudam.admin.presentation.response.user.UserPageAdminResponse
import com.sseudam.admin.presentation.v1.annotation.AdminTagDocs
import com.sseudam.admin.presentation.v1.annotation.ApiAdminV1Controller
import com.sseudam.support.cursor.OffsetPageRequest
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam

@AdminTagDocs
@ApiAdminV1Controller
class AdminUserController(
    private val adminFacade: AdminFacade,
) {
    /** 어드민 사용자 API */
    @Operation(summary = "사용자 리스트 조회", description = "사용자 리스트를 조회합니다.")
    @GetMapping("/users")
    fun findUsersByPage(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
    ): UserPageAdminResponse =
        UserPageAdminResponse.of(
            adminFacade.findUsers(OffsetPageRequest(page, size)),
        )

    @Operation(summary = "사용자 정보 조회", description = "사용자 정보를 조회합니다.")
    @GetMapping("/users/{userId}")
    fun findOneUser(
        @PathVariable("userId") userId: Long,
    ): AdminUserResponse = AdminUserResponse.of(adminFacade.findByUser(userId))
}
