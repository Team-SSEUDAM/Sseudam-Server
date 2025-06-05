package com.sseudam.suggestion

import com.sseudam.config.AwsProperties
import com.sseudam.s3.AwsS3Client
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.LocalDateTime

@Component
class SuggestionS3Processor(
    private val awsS3Client: AwsS3Client,
    private val awsProperties: AwsProperties,
    private val suggestionFileConstructor: SuggestionFileConstructor,
) : SuggestionS3Caller {
    companion object {
        const val IMAGE_ORIGIN_URL = "https://img.sseudam.me/"
    }

    override fun createUploadUrl(
        userId: Long,
        dateTime: LocalDateTime,
    ): SuggestionImageUrl {
        val imageFilePath = suggestionFileConstructor.imageFilePath(userId, dateTime)
        val imageFileName = suggestionFileConstructor.imageFileName(userId)

        val presignedUrl =
            awsS3Client.generateUploadUrl(
                awsProperties.s3.bucket,
                imageFilePath,
                imageFileName,
                Duration.ofSeconds(30),
            )
        return SuggestionImageUrl(
            presignedUrl,
            "$IMAGE_ORIGIN_URL$imageFilePath/$imageFileName",
        )
    }
}
