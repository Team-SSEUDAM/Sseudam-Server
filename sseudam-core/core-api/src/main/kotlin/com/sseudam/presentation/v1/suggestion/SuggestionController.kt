package com.sseudam.presentation.v1.suggestion

import com.sseudam.presentation.v1.annotation.ApiV1Controller
import com.sseudam.presentation.v1.suggestion.request.SpotSuggestionCreateRequest
import com.sseudam.presentation.v1.suggestion.response.SuggestionImageUrlResponse
import com.sseudam.suggestion.SpotSuggestion
import com.sseudam.suggestion.SuggestionService
import com.sseudam.user.User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "🤝 Suggestion API", description = "제보 관련 API")
@ApiV1Controller
class SuggestionController(
    private val suggestionService: SuggestionService,
) {
    @Operation(summary = "제보하기", description = "장소에 대한 제보를 합니다.")
    @PostMapping("/suggestions")
    fun suggestionSpotCreate(
        user: User,
        @RequestBody request: SpotSuggestionCreateRequest,
    ): SuggestionImageUrlResponse {
        val suggestion =
            suggestionService.createSuggestion(
                SpotSuggestion.Create(
                    user.id,
                    request.latitude,
                    request.longitude,
                    request.region,
                    request.city,
                    request.site,
                    request.trashType,
                ),
            )
        return SuggestionImageUrlResponse.of(suggestion.first, suggestion.second)
    }
}
