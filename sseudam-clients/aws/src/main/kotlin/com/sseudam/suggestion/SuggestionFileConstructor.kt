package com.sseudam.suggestion

import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Random

@Component
class SuggestionFileConstructor(
    private val environment: Environment,
) {
    companion object {
        const val SUGGESTION_IMAGE_PATH = "suggestion"
    }

    fun imageFileName(userId: Long): String = "${randomFileName()}.png"

    fun imageFilePath(
        userId: Long,
        dateTime: LocalDateTime,
    ): String {
        val profile = environment.activeProfiles.firstOrNull() ?: "dev"
        val dateTimeToLong = dateTime.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli()
        return "$profile/$SUGGESTION_IMAGE_PATH/$userId/$dateTimeToLong"
    }

    private fun randomFileName(): String {
        val leftLimit = 97
        val rightLimit = 122
        val targetStringLength = 24
        val random = Random()

        return random
            .ints(leftLimit, rightLimit + 1)
            .limit(targetStringLength.toLong())
            .collect({ StringBuilder() }, { sb, codePoint -> sb.appendCodePoint(codePoint) }, StringBuilder::append)
            .toString()
    }
}
