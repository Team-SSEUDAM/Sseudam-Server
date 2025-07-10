package com.sseudam.admin.presentation.request

import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType

data class AdminPushNotificationRequest(
    val topic: String,
    val contents: String,
) {
    init {
        require(topic.isEmpty()) { throw ErrorException(ErrorType.PUSH_NOTIFICATION_TOPIC_EMPTY) }
        require(contents.isEmpty()) { throw ErrorException(ErrorType.PUSH_NOTIFICATION_CONTENTS_EMPTY) }
        require(contents.length > 100) { throw ErrorException(ErrorType.PUSH_NOTIFICATION_CONTENTS_TOO_LONG) }
    }
}
