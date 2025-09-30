package com.sseudam.notification.discord

import com.sseudam.report.SpotReport
import com.sseudam.suggestion.SpotSuggestion
import com.sseudam.user.UserProfile

interface DiscordClient {
    fun sendCreateUserMessage(userProfile: UserProfile)

    fun sendReportMessage(report: SpotReport.Info)

    fun sendSuggestionMessage(suggestion: SpotSuggestion.Info)
}
