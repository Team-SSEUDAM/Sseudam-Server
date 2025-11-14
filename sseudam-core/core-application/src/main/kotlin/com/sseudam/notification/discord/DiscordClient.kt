package com.sseudam.notification.discord

import com.sseudam.notification.dto.SendMessageReportDto
import com.sseudam.notification.dto.SendMessageSuggestionDto
import com.sseudam.user.dto.SendMessageUserProfileDto

interface DiscordClient {
    fun sendCreateUserMessage(messageUserProfileDto: SendMessageUserProfileDto)

    fun sendReportMessage(messageReportDto: SendMessageReportDto)

    fun sendSuggestionMessage(messageSuggestionDto: SendMessageSuggestionDto)
}
