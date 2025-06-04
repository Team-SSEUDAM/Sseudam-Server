package com.sseudam.suggestion

import org.springframework.stereotype.Service

@Service
class SuggestionService(
    private val spotSuggestionAppender: SpotSuggestionAppender,
)
