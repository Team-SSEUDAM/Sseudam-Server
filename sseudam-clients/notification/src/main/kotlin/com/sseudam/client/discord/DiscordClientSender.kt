package com.sseudam.client.discord

import com.sseudam.notification.discord.DiscordClient
import com.sseudam.report.dto.SendMessageReportDto
import com.sseudam.suggestion.dto.SendMessageSuggestionDto
import com.sseudam.user.dto.SendMessageUserProfileDto
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
    companion object {
        const val ADMIN_URL = "https://sseudam-admin.vercel.app"
    }

    private val environmentName: String
        get() {
            val env = environment.getProperty("spring.profiles.active")
            return when (env) {
                "prod" -> "운영"
                "staging" -> "스테이징"
                "dev" -> "개발"
                else -> "그 외 환경"
            }
        }

    @Retryable(
        maxAttempts = 3,
        backoff = Backoff(delay = 1000, multiplier = 2.0),
        retryFor = [Exception::class],
    )
    override fun sendCreateUserMessage(messageUserProfileDto: SendMessageUserProfileDto) {
        val content =
            """
            ## 🎉 쓰담 새 회원 가입 알림 ($environmentName)

            **이메일:** ${messageUserProfileDto.email}
            **회원 ID:** ${messageUserProfileDto.id}
            **닉네임:** ${messageUserProfileDto.nickname}
            **사용자 관심지역:** ${messageUserProfileDto.site}
            **가입일시:** ${messageUserProfileDto.createdAt.toLocalDate()} ${messageUserProfileDto.createdAt.toLocalTime()}
            """.trimIndent()

        val message = DiscordMessagePayload(content)
        discordWebhookClient.sendMessage(URI.create(discordWebhookProperties.userChannel), message)
    }

    @Retryable(
        maxAttempts = 3,
        backoff = Backoff(delay = 1000, multiplier = 2.0),
        retryFor = [Exception::class],
    )
    override fun sendReportMessage(messageReportDto: SendMessageReportDto) {
        val content =
            """
            ## 🚨 신고 접수 알림 ($environmentName)

            **신고 ID:** ${messageReportDto.id}
            **신고 유형:** ${messageReportDto.reportType}
            ${messageReportDto.reportBody}
            **신고자 ID:** ${messageReportDto.userId}
            **신고자 닉네임:** ${messageReportDto.nickname}
            **신고 일시:** ${messageReportDto.createdAt.toLocalDate()} ${messageReportDto.createdAt.toLocalTime()}

            확인하러 가기 -> $ADMIN_URL/reports/trash-cans
            """.trimIndent()

        val message = DiscordMessagePayload(content)
        discordWebhookClient.sendMessage(URI.create(discordWebhookProperties.reportChannel), message)
    }

    @Retryable(
        maxAttempts = 3,
        backoff = Backoff(delay = 1000, multiplier = 2.0),
        retryFor = [Exception::class],
    )
    override fun sendSuggestionMessage(messageSuggestionDto: SendMessageSuggestionDto) {
        val content =
            """
            ## 💡 제보 접수 알림 ($environmentName)
            **제보 ID:** ${messageSuggestionDto.id}
            **제보된 주소:** ${messageSuggestionDto.site}
            **제보된 장소 이름:** ${messageSuggestionDto.spotName}
            **제보된 장소 좌표:** ${messageSuggestionDto.coordinateText}
            **제보된 쓰레기통 타입:** ${messageSuggestionDto.trashType}
            **제보자 ID:** ${messageSuggestionDto.userId}
            **제보자 닉네임:** ${messageSuggestionDto.nickname}
            **제보 일시:** ${messageSuggestionDto.createdAt.toLocalDate()} ${messageSuggestionDto.createdAt.toLocalTime()}
            확인하러 가기 -> $ADMIN_URL/suggestions/trash-cans
            """.trimIndent()
        val message = DiscordMessagePayload(content)
        discordWebhookClient.sendMessage(
            URI.create(discordWebhookProperties.suggestionChannel),
            message,
        )
    }
}
