package com.sseudam.presentation.v1

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
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
}
