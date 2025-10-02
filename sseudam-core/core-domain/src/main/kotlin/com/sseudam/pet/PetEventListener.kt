package com.sseudam.pet

import com.sseudam.pet.event.UserPetContextEvent
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
    @ApplicationModuleListener(id = "add-pet-point-history", condition = "#event.petPointAction != null")
    fun addPetPointHistory(event: UserPetContextEvent) {
        val userPet = userPetService.findByUser(event.userId) ?: return
        petPointHistoryService.append(userPet, event.petPointAction)
    }

    /** 레벨업 여부 결정 및 성장 기록 저장 */
    @ApplicationModuleListener(id = "user-pet-point-update", condition = "#event.petPointAction != null")
    fun addUserPetPoint(event: UserPetContextEvent) {
        val (currentYear, currentMonth) = LocalDateTime.now().run { year to month }
        val userPet = userPetService.findByUser(event.userId) ?: return
        val updateUserPet = userPetService.updatePointByAction(userPet, event.petPointAction)
        val petInfos = petService.findAllLatestSeasonPets(currentYear, currentMonth)
        val currentPetInfo = petService.findBy(updateUserPet.petId)
        val levelType = userPetPolicy.getLevelType(updateUserPet.point)

        if (currentPetInfo.levelType == Pet.LevelType.SPECIAL) return
        val nextLevelPetInfo = petInfos.firstOrNull { it.levelType == levelType } ?: return
        if (levelType.level > currentPetInfo.levelType.level) {
            Cache.put(
                key = "pet:${updateUserPet.userId}",
                value = userPetService.updatePetId(updateUserPet.id, nextLevelPetInfo.id),
                ttl = Cache.TTL_1_DAY,
            )
            petLevelUpHistoryService.append(updateUserPet, nextLevelPetInfo)
            Cache.delete(key = "pet:history:${updateUserPet.userId}")
        }
    }
}
