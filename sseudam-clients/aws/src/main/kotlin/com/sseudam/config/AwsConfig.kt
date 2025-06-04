package com.sseudam.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import java.net.URI

@Configuration
class AwsConfig(
    private val awsProperties: AwsProperties,
) {
    @Bean
    fun credentialProvider(): AwsCredentialsProvider =
        StaticCredentialsProvider.create(
            AwsBasicCredentials.create(awsProperties.credentials.accessKey, awsProperties.credentials.secretKey),
        )

    @Bean
    fun s3Client(): S3Client {
        val client =
            S3Client
                .builder()
                .credentialsProvider(credentialProvider())
                .region(Region.of(awsProperties.region))
        awsProperties.endpoint?.let {
            client.endpointOverride(URI.create(awsProperties.endpoint))
        }

        return client.build()
    }

    @Bean
    fun s3Presigner(): S3Presigner {
        val client =
            S3Presigner
                .builder()
                .credentialsProvider(credentialProvider())
                .region(Region.of(awsProperties.region))
        awsProperties.endpoint?.let {
            client.endpointOverride(URI.create(awsProperties.endpoint))
        }

        return client.build()
    }
}
