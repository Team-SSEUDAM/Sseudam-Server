package com.sseudam.suggestion.event

import com.sseudam.notification.discord.DiscordClient
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Component

@Component
class SpotSuggestionEventHandler(
    private val discordClient: DiscordClient,
) {
    @ApplicationModuleListener(id = "spot-suggestion-discord-notification")
    fun handleDiscordNotification(event: SpotSuggestionCreatedEvent) {
        discordClient.sendSuggestionMessage(event.spotSuggestion)
    }
}
