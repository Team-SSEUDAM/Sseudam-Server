package com.sseudam.common

data class S3ImageUrl(
    val presignedUrl: String,
    val imageUrl: String,
)
