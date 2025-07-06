package com.sseudam.notification

import org.springframework.stereotype.Component

@Component
class NotificationScheduler(
    private val notificationService: NotificationService,
)
