package com.sseudam.suggestion

import java.time.LocalDateTime

interface SuggestionS3Caller {
    fun createUploadUrl(
        userId: Long,
        dateTime: LocalDateTime,
    ): SuggestionImageUrl
}
