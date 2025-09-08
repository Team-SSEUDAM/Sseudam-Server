package com.sseudam.user

import com.sseudam.auth.AuthenticationService
import com.sseudam.pet.UserPetService
import com.sseudam.support.tx.TxAdvice
import org.springframework.stereotype.Service

@Service
class UserFacade(
    private val userService: UserService,
    private val authenticationService: AuthenticationService,
    private val userPetService: UserPetService,
    private val txAdvice: TxAdvice,
) {
    fun withdrawalUser(user: User) =
        txAdvice.write {
            userPetService.deleteByUser(user.id)
            userService.deleteUser(
                NewUserWithdrawal(
                    user = user,
                ),
            )
            authenticationService.withdrawUser(user.key)
        }
}
