package com.sseudam.application.user

import com.sseudam.DevelopTest
import com.sseudam.auth.AuthenticationService
import com.sseudam.pet.UserPetService
import com.sseudam.user.UserFacade
import com.sseudam.user.UserService
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.mockk

@DevelopTest
class UserFacadeTest :
    DescribeSpec({
        val userService: UserService = mockk()
        val authenticationService: AuthenticationService = mockk()
        val userPetService: UserPetService = mockk()
        val userFacade =
            UserFacade(
                userService = userService,
                authenticationService = authenticationService,
                userPetService = userPetService,
            )
    })
