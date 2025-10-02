package com.sseudam.suggestion.event

import com.sseudam.notification.discord.DiscordClient
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class SpotSuggestionEventHandler(
    private val discordClient: DiscordClient,
) {
    @EventListener
    fun handleDiscordNotification(event: SpotSuggestionCreatedEvent) {
        discordClient.sendSuggestionMessage(event.spotSuggestion)
    }
}
