package com.sseudam.common

interface ImageS3Caller {
    fun createUploadUrl(
        userId: Long,
        prefix: String,
    ): S3ImageUrl
}
