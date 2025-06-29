package com.sseudam.user

import com.sseudam.support.error.AuthenticationErrorException
import com.sseudam.support.error.AuthenticationErrorType
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import org.springframework.stereotype.Component

@Component
class UserValidator(
    private val userRepository: UserRepository,
) {
    companion object {
        /** 이메일 정규화 **/
        val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,}\$")
    }

    fun verifyEmail(email: String) {
        if (!email.matches(EMAIL_REGEX)) {
            throw AuthenticationErrorException(AuthenticationErrorType.INVALID_LOGIN_ID_FORMAT)
        }

        if (userRepository.existsByEmail(email)) {
            throw ErrorException(ErrorType.DUPLICATED_EMAIL)
        }
    }

    fun verifyNickname(nickname: String) {
        if (userRepository.existsByNickname(nickname)) {
            throw ErrorException(ErrorType.DUPLICATE_NICKNAME)
        }
    }
}
