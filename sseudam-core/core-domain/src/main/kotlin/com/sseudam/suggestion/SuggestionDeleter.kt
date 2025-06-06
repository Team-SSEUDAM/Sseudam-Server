package com.sseudam.suggestion

import org.springframework.stereotype.Component

@Component
class SuggestionDeleter(
    private val spotSuggestionRepository: SpotSuggestionRepository,
)
