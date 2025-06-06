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
        private val RANDOM = Random()
        private const val LOWERCASE_A = 97
        private const val LOWERCASE_Z = 122
        private const val FILENAME_LENGTH = 24
    }

    fun imageFileName(): String = "${randomFileName()}.png"

    fun imageFilePath(
        userId: Long,
        dateTime: LocalDateTime,
    ): String {
        val profile = environment.activeProfiles.firstOrNull() ?: "dev"
        val dateTimeToLong = dateTime.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli()
        return "$profile/$SUGGESTION_IMAGE_PATH/$userId/$dateTimeToLong"
    }

    private fun randomFileName(): String =
        (1..FILENAME_LENGTH)
            .map { RANDOM.nextInt(LOWERCASE_A, LOWERCASE_Z + 1).toChar() }
            .joinToString("")
}
