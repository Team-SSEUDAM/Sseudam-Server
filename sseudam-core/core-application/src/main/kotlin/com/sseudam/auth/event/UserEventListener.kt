package com.sseudam.auth.event

import com.sseudam.auth.AuthenticationService
import com.sseudam.user.event.UserWithdrawalEvent
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Component

@Component
class UserEventListener(
    private val authenticationService: AuthenticationService,
) {
    /**
     * 사용자 탈퇴 시 인증 정보를 삭제합니다.
     * @ApplicationModuleListener를 사용하여 모듈 간 이벤트 기반 통신을 구현합니다.
     */
    @ApplicationModuleListener
    fun handleUserWithdrawal(event: UserWithdrawalEvent) {
        authenticationService.withdrawUser(event.userKey)
    }
}
