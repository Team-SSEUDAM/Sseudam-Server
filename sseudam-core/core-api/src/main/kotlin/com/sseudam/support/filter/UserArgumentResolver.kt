package com.sseudam.support.filter

import com.sseudam.auth.Provider
import com.sseudam.user.User
import com.sseudam.user.UserService
import org.springframework.core.MethodParameter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class UserArgumentResolver(
    private val userService: UserService,
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean = parameter.parameterType == User::class.java

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        nativeWebRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): User? {
        val authentication = SecurityContextHolder.getContext().authentication.principal

        if (authentication != "anonymousUser") {
            val provider = authentication as Provider
            return userService.getUser(provider.userId)
        }
        return null
    }
}
