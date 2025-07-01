package com.sseudam.notification

import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType

enum class ReadStatus {
    READ,
    UNREAD,
    ;

    companion object {
        fun of(readStatus: String): ReadStatus =
            when (readStatus) {
                "READ" -> READ
                "UNREAD" -> UNREAD
                else -> throw ErrorException(ErrorType.METHOD_ARGUMENT_TYPE_MISMATCH)
            }
    }
}
