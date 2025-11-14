package com.sseudam.notification

import org.springframework.stereotype.Service

@Service
class NotificationFacade {
    // This facade is now intentionally minimal.
    // Batch notification operations have been moved to BatchNotificationService in the batch module
    // to avoid circular dependencies between notification and user modules.
}
