package com.sseudam.suggestion.event

import com.sseudam.pet.PetPointAction
import com.sseudam.suggestion.SpotSuggestion

data class SpotSuggestionCreatedEvent(
    val spotSuggestion: SpotSuggestion.Info,
    val userId: Long,
    val petPointAction: PetPointAction,
)
