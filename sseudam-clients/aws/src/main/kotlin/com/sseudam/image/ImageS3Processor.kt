package com.sseudam.image

import com.sseudam.common.ImageS3Caller
import com.sseudam.common.S3ImageUrl
import com.sseudam.config.AwsProperties
import com.sseudam.s3.AwsS3Client
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.LocalDateTime

@Component
class ImageS3Processor(
    private val awsS3Client: AwsS3Client,
    private val awsProperties: AwsProperties,
    private val imageFileConstructor: ImageFileConstructor,
) : ImageS3Caller {
    companion object {
        const val IMAGE_ORIGIN_URL = "https://img.sseudam.me/"
    }

    override fun createUploadUrl(
        userId: Long,
        dateTime: LocalDateTime,
        prefix: String,
    ): S3ImageUrl {
        val imageFilePath = imageFileConstructor.imageFilePath(userId, dateTime, prefix)
        val imageFileName = imageFileConstructor.imageFileName()

        val presignedUrl =
            awsS3Client.generateUploadUrl(
                awsProperties.s3.bucket,
                imageFilePath,
                imageFileName,
                Duration.ofSeconds(30),
            )
        return S3ImageUrl(
            presignedUrl,
            "$IMAGE_ORIGIN_URL$imageFilePath/$imageFileName",
        )
    }
}
