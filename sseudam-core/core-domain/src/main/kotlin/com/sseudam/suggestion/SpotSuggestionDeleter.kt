package com.sseudam.suggestion

import org.springframework.stereotype.Component

@Component
class SpotSuggestionDeleter(
    private val spotSuggestionRepository: SpotSuggestionRepository,
)
