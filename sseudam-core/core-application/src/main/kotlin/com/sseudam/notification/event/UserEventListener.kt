package com.sseudam.notification.event

import com.sseudam.notification.discord.DiscordClient
import com.sseudam.user.dto.SendMessageUserProfileDto
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Component

@Component
class UserEventListener(
    private val discordClient: DiscordClient,
) {
    @ApplicationModuleListener
    fun handleUserDiscordNotificationRequested(event: UserDiscordNotificationRequestedEvent) {
        discordClient.sendCreateUserMessage(
            SendMessageUserProfileDto(
                id = event.id,
                email = event.email,
                nickname = event.nickname,
                site = event.site,
                createdAt = event.createdAt,
            ),
        )
    }
}
