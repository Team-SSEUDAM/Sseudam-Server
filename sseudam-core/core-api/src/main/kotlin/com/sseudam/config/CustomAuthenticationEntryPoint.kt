package com.sseudam.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.sseudam.support.error.AuthenticationErrorType
import com.sseudam.support.error.ErrorResponse
import com.sseudam.support.response.ApiResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint

class CustomAuthenticationEntryPoint(
    private val objectMapper: ObjectMapper,
) : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authenticationException: AuthenticationException,
    ) {
        with(response) {
            status = HttpStatus.UNAUTHORIZED.value()
            contentType = MediaType.APPLICATION_JSON_VALUE
            writer.write(
                objectMapper.writeValueAsString(
                    ApiResponse.fail(
                        status = HttpStatus.UNAUTHORIZED.value(),
                        errorResponse =
                            ErrorResponse(
                                errorClassName = AuthenticationErrorType.UNAUTHORIZED_TOKEN::class.java.simpleName,
                                message = AuthenticationErrorType.UNAUTHORIZED_TOKEN.message,
                            ),
                    ),
                ),
            )
        }
    }
}
