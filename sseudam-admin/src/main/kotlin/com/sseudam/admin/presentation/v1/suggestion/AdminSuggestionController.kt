package com.sseudam.admin.presentation.v1.suggestion

import com.sseudam.admin.application.AdminFacade
import com.sseudam.admin.presentation.request.suggestion.AdminUpdateSuggestionRequest
import com.sseudam.admin.presentation.response.suggestion.SpotSuggestionAdminResponse
import com.sseudam.admin.presentation.response.suggestion.SpotSuggestionAllAdminResponse
import com.sseudam.admin.presentation.v1.annotation.AdminTagDocs
import com.sseudam.admin.presentation.v1.annotation.ApiAdminV1Controller
import com.sseudam.suggestion.SuggestionStatus
import com.sseudam.support.cursor.OffsetPageRequest
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

@AdminTagDocs
@ApiAdminV1Controller
class AdminSuggestionController(
    private val adminFacade: AdminFacade,
) {
    /** 어드민 제보 API */
    @Operation(summary = "제보 리스트 조회", description = "제보 리스트를 조회합니다.")
    @GetMapping("/suggestions")
    fun findSuggestionsByPage(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false) searchStatus: SuggestionStatus?,
    ): SpotSuggestionAllAdminResponse =
        SpotSuggestionAllAdminResponse.of(
            adminFacade.findSuggestions(OffsetPageRequest(page, size), searchStatus),
        )

    @Operation(summary = "제보 내역 상세 조회", description = "제보 내역을 상세 조회합니다.")
    @GetMapping("/suggestions/{suggestionId}")
    fun findSuggestionDetails(
        @PathVariable suggestionId: Long,
    ): SpotSuggestionAdminResponse = SpotSuggestionAdminResponse.of(adminFacade.findSuggestionDetails(suggestionId))

    @Operation(summary = "제보 반영", description = "제보 상태 변경과 생성을 합니다.")
    @PutMapping("/suggestions/{suggestionId}")
    fun updateSuggestionStatus(
        @PathVariable suggestionId: Long,
        @RequestBody request: AdminUpdateSuggestionRequest,
    ) = adminFacade.updateSpotSuggestionStatus(request.toCommand(suggestionId))
}
