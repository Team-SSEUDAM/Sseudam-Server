package com.sseudam.user

import com.sseudam.auth.AuthenticationService
import com.sseudam.pet.UserPetService
import com.sseudam.support.tx.Tx
import com.sseudam.user.command.UserWithdrawalCommand
import org.springframework.stereotype.Service

@Service
class UserFacade(
    private val userService: UserService,
    private val authenticationService: AuthenticationService,
    private val userPetService: UserPetService,
) {
    fun withdrawalUser(user: User) =
        Tx.writeable {
            userPetService.deleteByUser(user.id)
            userService.deleteUser(
                UserWithdrawalCommand(
                    user = user,
                ),
            )
            authenticationService.withdrawUser(user.key)
        }
}
