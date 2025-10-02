package com.sseudam.report.event

import com.sseudam.notification.discord.DiscordClient
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Component

@Component
class SpotReportEventHandler(
    private val discordClient: DiscordClient,
) {
    @ApplicationModuleListener(id = "spot-report-discord-notification")
    fun handleDiscordNotification(event: SpotReportCreatedEvent) {
        discordClient.sendReportMessage(event.spotReport)
    }
}
