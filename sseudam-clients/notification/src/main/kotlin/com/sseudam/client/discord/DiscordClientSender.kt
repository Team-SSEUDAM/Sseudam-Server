package com.sseudam.client.discord

import com.sseudam.notification.discord.DiscordClient
import com.sseudam.user.UserProfile
import org.springframework.core.env.Environment
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component
import java.net.URI

@Component
class DiscordClientSender(
    private val environment: Environment,
    private val discordWebhookClient: DiscordWebhookClient,
    private val discordWebhookProperties: DiscordWebhookProperties,
) : DiscordClient {
    @Retryable(
        maxAttempts = 3,
        backoff = Backoff(delay = 1000, multiplier = 2.0),
        retryFor = [Exception::class],
    )
    override fun sendCreateUserMessage(userProfile: UserProfile) {
        val environment = environment.getProperty("spring.profiles.active")
        val environmentName =
            when (environment) {
                "prod" -> "운영"
                "staging" -> "스테이징"
                "dev" -> "개발"
                else -> "그 외 환경"
            }
        val content =
            """
            ## 🎉 쓰담 새 회원 가입 알림 ($environmentName)

            **닉네임:** ${userProfile.nickname}
            **이메일:** ${userProfile.email}
            **이름:** ${userProfile.name ?: "Unknown"}
            **회원 ID:** ${userProfile.id}
            **가입일시:** ${userProfile.createdAt.toLocalDate()} ${userProfile.createdAt.toLocalTime()}
            """.trimIndent()

        val message = DiscordMessagePayload(content)
        discordWebhookClient.sendMessage(URI.create(discordWebhookProperties.userChannel), message)
    }
}
