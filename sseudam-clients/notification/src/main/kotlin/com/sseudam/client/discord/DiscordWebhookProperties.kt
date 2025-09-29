package com.sseudam.client.discord

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "discord.webhook.alert")
data class DiscordWebhookProperties(
    val userChannel: String,
    val reportChannel: String,
    val suggestionChannel: String,
)
