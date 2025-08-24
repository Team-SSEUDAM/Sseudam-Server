package com.sseudam.suggestion

import com.sseudam.common.S3ImageUrl
import com.sseudam.support.Cache
import com.sseudam.trashspot.TrashSpotService
import org.springframework.stereotype.Service

@Service
class SuggestionFacade(
    private val suggestionService: SuggestionService,
    private val trashSpotService: TrashSpotService,
) {
    fun validateSpotSuggestion(name: String): Boolean {
        suggestionService.validateSpotSuggestionName(name)
        trashSpotService.validateSpotName(name)
        return true
    }

    fun createSpotSuggestion(create: SpotSuggestion.Create): Pair<SpotSuggestion.Info, S3ImageUrl> {
        trashSpotService.appendVerifySpot(create.site, create.longitude, create.latitude)
        val (suggestionInfo, s3ImageUrl) =
            suggestionService
                .append(create)
                .apply {
                    Cache.delete("user:${create.userId}:histories")
                }
        return suggestionInfo to s3ImageUrl
    }
}
