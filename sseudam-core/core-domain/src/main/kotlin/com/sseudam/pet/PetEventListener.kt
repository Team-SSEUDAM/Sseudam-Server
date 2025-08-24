package com.sseudam.pet

import com.sseudam.pet.event.PetPointEvent
import com.sseudam.support.Cache
import org.springframework.modulith.events.ApplicationModuleListener
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
    @ApplicationModuleListener(condition = "#event.petPointAction != null")
    fun addPetPointHistory(event: PetPointEvent) {
        petPointHistoryService.append(event.userPet, event.petPointAction)
    }

    /** 레벨업 여부 결정 및 성장 기록 저장 */
    @ApplicationModuleListener(condition = "#event.petPointAction != null")
    fun addUserPetPoint(event: PetPointEvent) {
        val (currentYear, currentMonth) = LocalDateTime.now().run { year to month }
        val userPet = userPetService.updatePointByAction(event.userPet, event.petPointAction)
        val petInfos = petService.findAllLatestSeasonPets(currentYear, currentMonth)
        val currentPetInfo = petService.findBy(userPet.petId)
        val levelType = userPetPolicy.getLevelType(userPet.point)

        if (currentPetInfo.levelType == Pet.LevelType.SPECIAL) return
        val nextLevelPetInfo = petInfos.firstOrNull { it.levelType == levelType } ?: return
        if (levelType.level > currentPetInfo.levelType.level) {
            Cache.put(
                key = "pet:${userPet.userId}",
                value = userPetService.updatePetId(userPet.id, nextLevelPetInfo.id),
                ttl = Cache.TTL_1_DAY,
            )
            petLevelUpHistoryService.append(userPet, nextLevelPetInfo)
            Cache.delete(key = "pet:history:${userPet.userId}")
        }
    }
}
