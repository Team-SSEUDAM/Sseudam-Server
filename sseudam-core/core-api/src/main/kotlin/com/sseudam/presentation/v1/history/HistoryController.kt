package com.sseudam.presentation.v1.history

import com.sseudam.history.HistoryFacade
import com.sseudam.history.SpotActionType
import com.sseudam.presentation.v1.annotation.ApiV1Controller
import com.sseudam.presentation.v1.history.response.HistoryAllResponse
import com.sseudam.user.User
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@ApiV1Controller
class HistoryController(
    private val historyFacade: HistoryFacade,
) {
    @Operation(summary = "내역 조회", description = "제보/신고 내역을 조회합니다.")
    @GetMapping("/histories")
    fun histories(
        user: User,
        @RequestParam action: SpotActionType?,
    ): HistoryAllResponse = HistoryAllResponse.from(historyFacade.findHistories(user.id, action))
}
