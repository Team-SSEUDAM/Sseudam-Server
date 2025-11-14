package com.sseudam.notification.event

import com.sseudam.notification.NotificationFacade
import com.sseudam.pet.event.NewPetSeasonCreatedEvent
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Component

@Component
class PetEventListener(
    private val notificationFacade: NotificationFacade,
) {
    /**
     * 새로운 펫 시즌이 생성되었을 때 알림을 발송합니다.
     * @ApplicationModuleListener를 사용하여 모듈 간 이벤트 기반 통신을 구현합니다.
     */
    @ApplicationModuleListener
    fun handleNewPetSeasonCreated(event: NewPetSeasonCreatedEvent) {
        notificationFacade.sendNewPetNotifications()
    }
}
