package com.sseudam.common

import java.time.LocalDateTime

interface ImageS3Caller {
    fun createUploadUrl(
        userId: Long,
        dateTime: LocalDateTime,
        prefix: String,
    ): S3ImageUrl
}
