package com.sseudam.admin.presentation.request.suggestion

import com.sseudam.suggestion.SuggestionStatus
import com.sseudam.suggestion.UpdateSuggestionCommand
import io.swagger.v3.oas.annotations.media.Schema

data class AdminUpdateSuggestionRequest(
    @Schema(description = "반려 사유", example = "장소가 다름.")
    val reason: String? = null,
    @Schema(description = "제보 상태", example = "APPROVE")
    val status: SuggestionStatus,
) {
    fun toCommand(suggestionId: Long) =
        UpdateSuggestionCommand(
            suggestionId = suggestionId,
            reason = reason,
            status = status,
        )
}
