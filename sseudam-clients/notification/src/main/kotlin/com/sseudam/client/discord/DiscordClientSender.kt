package com.sseudam.client.discord

import com.sseudam.common.GeoJson
import com.sseudam.notification.discord.DiscordClient
import com.sseudam.report.ReportType
import com.sseudam.report.SpotReport
import com.sseudam.suggestion.SpotSuggestion
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
    override fun sendCreateUserMessage(userProfile: UserProfile) {
        val content =
            """
            ## 🎉 쓰담 새 회원 가입 알림 ($environmentName)

            **이메일:** ${userProfile.email}
            **회원 ID:** ${userProfile.id}
            **닉네임:** ${userProfile.nickname}
            **이름:** ${userProfile.name}
            **사용자 관심지역:** ${userProfile.address.site}
            **가입일시:** ${userProfile.createdAt.toLocalDate()} ${userProfile.createdAt.toLocalTime()}
            """.trimIndent()

        val message = DiscordMessagePayload(content)
        discordWebhookClient.sendMessage(URI.create(discordWebhookProperties.userChannel), message)
    }

    @Retryable(
        maxAttempts = 3,
        backoff = Backoff(delay = 1000, multiplier = 2.0),
        retryFor = [Exception::class],
    )
    override fun sendReportMessage(report: SpotReport.Info) {
        val reportMessageByType = "**신고 내용:** ${
            when (report.reportType) {
                ReportType.POINT -> {
                    when (val point = report.point) {
                        is GeoJson.Point -> {
                            val coords = point.coordinates
                            if (coords.size >= 2) {
                                "${coords[0]}, ${coords[1]} (경도, 위도)"
                            } else {
                                "좌표 정보 없음"
                            }
                        }
                        else -> "좌표 정보 없음"
                    }
                }
                ReportType.NAME -> report.spotName
                ReportType.KIND -> report.trashType.displayName
                ReportType.PHOTO -> report.imageUrl
            }
        }로 수정 요청"
        val content =
            """
            ## 🚨 신고 접수 알림 ($environmentName)

            **신고 ID:** ${report.id}
            **신고 유형:** ${report.reportType.displayName} (${report.reportType.name})
            $reportMessageByType
            **신고자 ID:** ${report.userId}
            **신고 일시:** ${report.createdAt.toLocalDate()} ${report.createdAt.toLocalTime()}

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
    override fun sendSuggestionMessage(suggestion: SpotSuggestion.Info) {
        val pointCoordinate =
            when (val point = suggestion.point) {
                is GeoJson.Point -> point.coordinates
                else -> emptyList()
            }
        val coordinateText =
            if (pointCoordinate.size >= 2) {
                "${pointCoordinate[0]}, ${pointCoordinate[1]} (경도, 위도)"
            } else {
                "좌표 정보 없음"
            }
        val content =
            """
            ## 💡 제보 접수 알림 ($environmentName)
            **제보 ID:** ${suggestion.id}
            **제보된 주소:** ${suggestion.address.site}
            **제보된 장소 이름:** ${suggestion.spotName}
            **제보된 장소 좌표:** $coordinateText
            **제보된 쓰레기통 타입:** ${suggestion.trashType.displayName}
            **제보자 ID:** ${suggestion.userId}
            **제보 일시:** ${suggestion.createdAt.toLocalDate()} ${suggestion.createdAt.toLocalTime()}
            확인하러 가기 -> $ADMIN_URL/suggestions/trash-cans
            """.trimIndent()
        val message = DiscordMessagePayload(content)
        discordWebhookClient.sendMessage(
            URI.create(discordWebhookProperties.suggestionChannel),
            message,
        )
    }
}
