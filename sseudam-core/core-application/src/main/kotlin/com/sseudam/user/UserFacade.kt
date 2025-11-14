package com.sseudam.user

import com.sseudam.pet.UserPetService
import com.sseudam.user.command.UserWithdrawalCommand
import com.sseudam.user.event.UserWithdrawalEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserFacade(
    private val userService: UserService,
    private val userPetService: UserPetService,
    private val eventPublisher: ApplicationEventPublisher,
) {
    @Transactional
    fun withdrawalUser(user: User) {
        userPetService.deleteByUser(user.id)
        userService.deleteUser(
            UserWithdrawalCommand(
                user = user,
            ),
        )

        // 이벤트 발행으로 auth 모듈과의 직접 의존성 제거
        eventPublisher.publishEvent(UserWithdrawalEvent(userKey = user.key))
    }
}
