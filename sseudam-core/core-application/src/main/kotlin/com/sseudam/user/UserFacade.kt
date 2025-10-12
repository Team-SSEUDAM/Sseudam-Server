package com.sseudam.user

import com.sseudam.auth.AuthenticationService
import com.sseudam.pet.UserPetService
import com.sseudam.user.command.UserWithdrawalCommand
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserFacade(
    private val userService: UserService,
    private val authenticationService: AuthenticationService,
    private val userPetService: UserPetService,
) {
    @Transactional
    fun withdrawalUser(user: User) {
        userPetService.deleteByUser(user.id)
        userService.deleteUser(
            UserWithdrawalCommand(
                user = user,
            ),
        )
        authenticationService.withdrawUser(user.key)
    }
}
