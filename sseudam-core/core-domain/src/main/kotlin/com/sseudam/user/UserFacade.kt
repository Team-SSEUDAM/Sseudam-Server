package com.sseudam.user

import com.sseudam.auth.AuthenticationService
import com.sseudam.pet.UserPetService
import org.springframework.stereotype.Service

@Service
class UserFacade(
    private val userService: UserService,
    private val authenticationService: AuthenticationService,
    private val userPetService: UserPetService,
) {
    fun withdrawalUser(user: User) {
        userPetService.deleteByUser(user.id)
        userService.deleteUser(
            NewUserWithdrawal(
                user = user,
            ),
        )
        authenticationService.withdrawUser(user.key)
    }
}
