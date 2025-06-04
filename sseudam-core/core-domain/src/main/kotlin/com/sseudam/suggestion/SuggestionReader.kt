package com.sseudam.suggestion

import org.springframework.stereotype.Component

@Component
class SuggestionReader(
    private val spotSuggestionRepository: SpotSuggestionRepository,
)
