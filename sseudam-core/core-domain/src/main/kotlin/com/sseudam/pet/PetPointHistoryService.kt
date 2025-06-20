package com.sseudam.pet

import org.springframework.stereotype.Service

@Service
class PetPointHistoryService(
    private val petPointHistoryAppender: PetPointHistoryAppender,
    private val petPointHistoryReader: PetPointHistoryReader,
) {
    fun findAllByUserPet(userPetId: Long): List<PetPointHistory.Info> = petPointHistoryReader.readAllByUserPet(userPetId)
}
