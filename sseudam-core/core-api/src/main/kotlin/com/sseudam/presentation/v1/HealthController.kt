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
    @ExceptionHandler(*[IllegalArgumentException::class, MethodArgumentNotValidException::class])
    fun handleException(exception: Exception): ErrorResponse {
        Sentry.captureException(exception)
        if (exception is MethodArgumentNotValidException) {
            val bindingResult = exception.bindingResult
            val errors = bindingResult.allErrors
            return ErrorResponse.of(errors.first().defaultMessage.toString(), "Method Argument Not Valid")
        } else {
            return ErrorResponse.of(exception.javaClass.name, "Sentry Error")
        }
    }
}
