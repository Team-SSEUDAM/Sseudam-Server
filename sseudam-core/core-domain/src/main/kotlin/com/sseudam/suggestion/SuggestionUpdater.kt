package com.sseudam.suggestion

import org.springframework.stereotype.Component

@Component
class SuggestionUpdater(
    private val spotSuggestionRepository: SpotSuggestionRepository,
)
