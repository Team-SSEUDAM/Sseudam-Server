package com.sseudam.presentation.v1.history

import com.sseudam.history.HistoryFacade
import com.sseudam.presentation.v1.annotation.ApiV1Controller
import com.sseudam.presentation.v1.history.response.HistoryAllResponse
import com.sseudam.user.User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping

@Tag(name = "📑 History API", description = "내역 관련 API 입니다.")
@ApiV1Controller
class HistoryController(
    private val historyFacade: HistoryFacade,
) {
    @Operation(summary = "내역 조회", description = "제보/신고 내역을 조회합니다.")
    @GetMapping("/histories")
    fun histories(user: User): HistoryAllResponse = HistoryAllResponse.from(historyFacade.findHistories(user.id))
}
