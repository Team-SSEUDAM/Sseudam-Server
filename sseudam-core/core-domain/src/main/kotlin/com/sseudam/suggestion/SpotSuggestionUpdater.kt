package com.sseudam.suggestion

import org.springframework.stereotype.Component

@Component
class SpotSuggestionUpdater(
    private val spotSuggestionRepository: SpotSuggestionRepository,
)
