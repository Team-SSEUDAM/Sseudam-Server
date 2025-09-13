package com.sseudam

data class ErrorResponse(
    val errorClassName: String,
    val message: String,
) {
    companion object {
        fun of(
            errorClassName: String,
            message: String,
        ) = ErrorResponse(
            errorClassName = errorClassName,
            message = message,
        )
    }
}
