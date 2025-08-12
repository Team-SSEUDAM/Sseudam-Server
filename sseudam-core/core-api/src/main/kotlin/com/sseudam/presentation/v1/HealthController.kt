package com.sseudam.presentation.v1

import com.sseudam.support.error.ErrorResponse
import io.sentry.Sentry
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@Tag(name = "\uD83C\uDFC3 Health Check", description = "서버 상태 확인 API")
@RestController
class HealthController {
    @GetMapping("/ping")
    @Operation(summary = "서버 상태 확인", description = "서버 상태를 확인합니다.")
    fun healthCheck(): PongResponse = PongResponse(LocalDateTime.now())

    data class PongResponse(
        val now: LocalDateTime,
    )

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = [IllegalArgumentException::class, MethodArgumentNotValidException::class])
    fun handleException(exception: Exception): ErrorResponse =
        when (exception) {
            is MethodArgumentNotValidException -> {
                val fieldErrors = exception.bindingResult.fieldErrors
                val message = fieldErrors.firstOrNull()?.defaultMessage ?: "Validation failed"
                ErrorResponse.of(message, "Method Argument Not Valid")
            }
            else -> {
                // Only capture non-validation exceptions to reduce noise.
                Sentry.captureException(exception)
                ErrorResponse.of(exception.message ?: exception.javaClass.simpleName, "Sentry Error")
            }
        }
}
