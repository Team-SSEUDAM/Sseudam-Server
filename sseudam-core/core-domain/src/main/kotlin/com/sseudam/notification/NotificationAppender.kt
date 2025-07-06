package com.sseudam.notification

import org.springframework.stereotype.Component

@Component
class NotificationAppender(
    private val notificationStoredRepository: NotificationStoredRepository,
)
