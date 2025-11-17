package com.sseudam.client.discord

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.net.URI

@FeignClient(name = "discord-webhook", url = "placeholder")
interface DiscordWebhookClient {
    @PostMapping
    fun sendMessage(
        uri: URI,
        @RequestBody message: DiscordMessagePayload,
    )
}
