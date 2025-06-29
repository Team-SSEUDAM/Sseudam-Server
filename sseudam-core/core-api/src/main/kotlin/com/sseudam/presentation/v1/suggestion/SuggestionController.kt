package com.sseudam.presentation.v1.suggestion

import com.sseudam.presentation.v1.annotation.ApiV1Controller
import com.sseudam.presentation.v1.report.response.ReportValidationResponse
import com.sseudam.presentation.v1.suggestion.request.SpotSuggestionCreateRequest
import com.sseudam.presentation.v1.suggestion.request.SuggestionValidationRequest
import com.sseudam.presentation.v1.suggestion.response.SpotSuggestionAllResponse
import com.sseudam.presentation.v1.suggestion.response.SuggestionImageUrlResponse
import com.sseudam.suggestion.SpotSuggestion
import com.sseudam.suggestion.SuggestionFacade
import com.sseudam.suggestion.SuggestionService
import com.sseudam.user.User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "🤝 Suggestion API", description = "제보 관련 API")
@ApiV1Controller
class SuggestionController(
    private val suggestionService: SuggestionService,
    private val suggestionFacade: SuggestionFacade,
) {
    @Operation(summary = "제보하기", description = "장소에 대한 제보를 합니다.")
    @PostMapping("/suggestions")
    fun suggestionSpotCreate(
        user: User,
        @RequestBody request: SpotSuggestionCreateRequest,
    ): SuggestionImageUrlResponse {
        val suggestion =
            suggestionService.createSpotSuggestion(
                SpotSuggestion.Create(
                    userId = user.id,
                    spotName = request.spotName,
                    latitude = request.latitude,
                    longitude = request.longitude,
                    region = request.region,
                    city = request.city,
                    site = request.site,
                    trashType = request.trashType,
                ),
            )
        return SuggestionImageUrlResponse.of(suggestion.first, suggestion.second)
    }

    // TODO: cursor pagination (infinity scroll)
    @Operation(summary = "사용자 제보 내역 조회", description = "사용자 제보 내역을 조회합니다.")
    @GetMapping("/suggestions")
    fun suggestionSpotFindAll(user: User): SpotSuggestionAllResponse {
        val suggestions = suggestionService.findAllSpotSuggestionByUser(user.id)
        return SpotSuggestionAllResponse.of(suggestions)
    }

    @Operation(summary = "제보 시 쓰레기통 검증", description = "제보 시 쓰레기통을 검증합니다.")
    @GetMapping("/suggestions/validate")
    fun validateSpotReport(
        user: User,
        @RequestBody request: SuggestionValidationRequest,
    ): ReportValidationResponse {
        val isValid = suggestionFacade.validateSpotReport(request.name)
        return ReportValidationResponse.of(isValid)
    }
}
