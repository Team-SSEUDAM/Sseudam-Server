package com.sseudam.visit.event

data class VisitedSpotNotificationRequestedEvent(
    val suggesterId: Long?,
    val title: String,
    val body: String,
    val destination: String,
    val notificationType: String,
    val parameterValue: String,
)
