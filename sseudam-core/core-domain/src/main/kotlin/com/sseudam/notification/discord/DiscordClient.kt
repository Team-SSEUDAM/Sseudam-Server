package com.sseudam.notification.discord

import com.sseudam.user.UserProfile

interface DiscordClient {
    fun sendCreateUserMessage(userProfile: UserProfile)
}
