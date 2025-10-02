package com.sseudam.report.event

import com.sseudam.notification.discord.DiscordClient
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class SpotReportEventHandler(
    private val discordClient: DiscordClient,
) {
    @EventListener
    fun handleDiscordNotification(event: SpotReportCreatedEvent) {
        discordClient.sendReportMessage(event.spotReport)
    }
}
