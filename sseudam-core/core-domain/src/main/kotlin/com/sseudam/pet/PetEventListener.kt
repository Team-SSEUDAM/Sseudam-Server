package com.sseudam.pet

import com.sseudam.pet.event.PetPointEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class PetEventListener(
    private val petService: PetService,
    private val userPetPolicy: UserPetPolicy,
    private val userPetService: UserPetService,
    private val petPointHistoryService: PetPointHistoryService,
    private val petLevelUpHistoryService: PetLevelUpHistoryService,
) {
    /** 포인트 지급 기록 저장 */
    @Async
    @EventListener
    fun addPetPointHistory(event: PetPointEvent) {
        petPointHistoryService.append(
            userPet = event.userPet,
            action = event.petPointAction,
        )
    }

    /** 레벨업 여부 결정 및 성장 기록 저장 */
    @Async
    @EventListener
    fun addUserPetPoint(event: PetPointEvent) {
        val (currentYear, currentMonth) = LocalDateTime.now().let { it.year to it.month }
        val userPet =
            userPetService.updatePoint(
                userPet = event.userPet,
                action = event.petPointAction,
            )

        val petInfos = petService.findAllLatestSeasonPets(currentYear, currentMonth)
        val currentPetInfo = petService.findBy(userPet.petId)

        val levelType = userPetPolicy.getLevelType(userPet.point)

        if (levelType.level >= Pet.LevelType.SPECIAL.level) return

        val nextLevelPetInfo =
            petInfos
                .firstOrNull { it.levelType.level == levelType.level }
                ?: return

        val levelUpType = userPetPolicy.levelUp(userPet = userPet)
        if (levelUpType.level > currentPetInfo.levelType.level) {
            userPetService.updatePetId(
                userPetId = userPet.id,
                petId = nextLevelPetInfo.id,
            )
            petLevelUpHistoryService.append(
                userPet = userPet,
                petInfo = nextLevelPetInfo,
            )
        }
    }
}
