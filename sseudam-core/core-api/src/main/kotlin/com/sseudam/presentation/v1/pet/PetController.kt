package com.sseudam.presentation.v1.pet

import com.sseudam.pet.PetService
import com.sseudam.pet.UserPetFacade
import com.sseudam.pet.UserPetPolicy
import com.sseudam.pet.UserPetService
import com.sseudam.presentation.v1.annotation.ApiV1Controller
import com.sseudam.presentation.v1.pet.request.UpdateUserPetNameRequest
import com.sseudam.presentation.v1.pet.response.UserPetInfoResponse
import com.sseudam.presentation.v1.pet.response.UserPetLevelHistoryAllResponse
import com.sseudam.presentation.v1.pet.response.UserPetLevelHistoryCurrentSeasonAllResponse
import com.sseudam.user.User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "😽 Pet API", description = "펫 관련 API")
@ApiV1Controller
class PetController(
    private val petService: PetService,
    private val userPetService: UserPetService,
    private val userPetFacade: UserPetFacade,
    private val userPetPolicy: UserPetPolicy,
) {
    @Operation(summary = "펫 정보 조회", description = "사용자의 펫 정보를 조회합니다.")
    @GetMapping("/pets")
    fun findUserPetInfo(user: User): UserPetInfoResponse {
        val userPetInfo = userPetFacade.findPetInfo(user.id)
        val petInfo = petService.findBy(userPetInfo.petId)

        val petLevel = userPetPolicy.getLevelType(userPetInfo.point)
        val maxLevelStandard = userPetPolicy.getMaxLevelStandard(petLevel)
        val userPetSeason = userPetPolicy.getSeasonByPetInfo(petInfo)

        return UserPetInfoResponse.of(userPetInfo, petLevel, userPetSeason, maxLevelStandard)
    }

    @Operation(summary = "펫 이름 변경", description = "사용자 펫의 이름을 변경합니다.")
    @PutMapping("/pets/name")
    fun updateUserPetName(
        user: User,
        @RequestBody
        request: UpdateUserPetNameRequest,
    ): UserPetInfoResponse {
        val updatedPetInfo = userPetService.updatePetName(user.id, request.nickname)
        val petInfo = petService.findBy(updatedPetInfo.petId)

        val petLevel = userPetPolicy.getLevelType(updatedPetInfo.point)
        val maxLevelStandard = userPetPolicy.getMaxLevelStandard(petLevel)
        val userPetSeason = userPetPolicy.getSeasonByPetInfo(petInfo)

        return UserPetInfoResponse.of(updatedPetInfo, petLevel, userPetSeason, maxLevelStandard)
    }

    @Operation(summary = "현 시즌 펫 성장 기록 조회", description = "사용자의 현 시즌 펫 성장 기록을 조회합니다.")
    @GetMapping("/pets/season")
    fun findUserPetSeasonInfo(user: User): UserPetLevelHistoryCurrentSeasonAllResponse {
        val seasonHistory = userPetFacade.findCurrentSeasonPetHistory(user.id)
        val petInfo = petService.findBy(seasonHistory.userPetInfo.petId)

        val petLevel = userPetPolicy.getLevelType(seasonHistory.userPetInfo.point)
        val maxLevelStandard = userPetPolicy.getMaxLevelStandard(petLevel)
        val season = userPetPolicy.getSeasonByPetInfo(petInfo)
        return UserPetLevelHistoryCurrentSeasonAllResponse.of(
            UserPetInfoResponse.of(seasonHistory.userPetInfo, petLevel, season, maxLevelStandard),
            seasonHistory.seasonHistory,
        )
    }

    @Operation(summary = "사용자 전체 펫 성장 기록 조회", description = "사용자의 전체 펫 성장 기록을 조회합니다.")
    @GetMapping("/pets/history")
    fun findUserPetHistory(user: User): UserPetLevelHistoryAllResponse {
        val histories = userPetFacade.findAllPetHistory(user.id)
        return UserPetLevelHistoryAllResponse.of(histories)
    }
}
