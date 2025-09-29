package com.sseudam.admin.presentation.request.suggestion

import com.sseudam.suggestion.SuggestionStatus
import com.sseudam.suggestion.UpdateSuggestionCommand

data class AdminUpdateSuggestionRequest(
    val reason: String?,
    val status: SuggestionStatus,
) {
    fun toCommand(suggestionId: Long) =
        UpdateSuggestionCommand(
            suggestionId = suggestionId,
            reason = reason,
            status = status,
        )
}
