package com.sseudam.presentation.v1.report.request

import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "신고 시 쓰레기통 검증 요청 Json")
data class ReportValidationRequest(
    val name: String,
) {
    init {
        validateName(name)
    }

    private fun validateName(name: String) {
        when {
            name.isBlank() -> throw ErrorException(ErrorType.SPOT_NAME_IS_BLANK)
            name.length < 2 -> throw ErrorException(ErrorType.INVALID_TRASH_SPOT_NAME_TWO)
            name.length > 12 -> throw ErrorException(ErrorType.INVALID_TRASH_SPOT_NAME_TWELVE)
        }
    }
}
