package com.sseudam.admin.presentation.request

import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType

data class AdminPushNotificationRequest(
    val topic: String,
    val contents: String,
) {
    init {
        require(topic.isNotEmpty()) { throw ErrorException(ErrorType.PUSH_NOTIFICATION_TOPIC_EMPTY) }
        require(contents.isNotEmpty()) { throw ErrorException(ErrorType.PUSH_NOTIFICATION_CONTENTS_EMPTY) }
        require(contents.length <= 60) { throw ErrorException(ErrorType.PUSH_NOTIFICATION_CONTENTS_TOO_LONG) }
    }
}
