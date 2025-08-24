package com.sseudam.image

import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import java.util.Random

@Component
class ImageFileConstructor(
    private val environment: Environment,
) {
    companion object {
        private val RANDOM = Random()
        private const val LOWERCASE_A = 97
        private const val LOWERCASE_Z = 122
        private const val FILENAME_LENGTH = 24
    }

    fun imageFileName(): String = "${randomFileName()}.jpeg"

    fun imageFilePath(
        userId: Long,
        prefix: String,
    ): String {
        val profile = environment.activeProfiles.firstOrNull() ?: "dev"
        return "$profile/$prefix/$userId"
    }

    private fun randomFileName(): String =
        (1..FILENAME_LENGTH)
            .map { RANDOM.nextInt(LOWERCASE_A, LOWERCASE_Z + 1).toChar() }
            .joinToString("")
}
