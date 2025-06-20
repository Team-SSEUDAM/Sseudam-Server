package com.sseudam.presentation.v1.pet

import com.sseudam.pet.UserPetFacade
import com.sseudam.pet.UserPetPolicy
import com.sseudam.pet.UserPetService
import com.sseudam.presentation.v1.annotation.ApiV1Controller
import com.sseudam.presentation.v1.pet.request.UpdateUserPetNameRequest
import com.sseudam.presentation.v1.pet.response.UserPetInfoResponse
import com.sseudam.presentation.v1.pet.response.UserPetLevelHistoryAllResponse
import com.sseudam.user.User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "😽 Pet API", description = "펫 관련 API")
@ApiV1Controller
class PetController(
    private val userPetService: UserPetService,
    private val userPetFacade: UserPetFacade,
    private val userPetPolicy: UserPetPolicy,
) {
    @Operation(summary = "펫 정보 조회", description = "사용자의 펫 정보를 조회합니다.")
    @GetMapping("/pets")
    fun findUserPetInfo(user: User): UserPetInfoResponse {
        val petInfo = userPetFacade.findPetInfo(user.id)
        val petLevel = userPetPolicy.getLevelType(petInfo.point)
        val maxLevelStandard = userPetPolicy.getMaxLevelStandard(petLevel)
        return UserPetInfoResponse.of(petInfo, petLevel, maxLevelStandard)
    }

    @Operation(summary = "펫 이름 변경", description = "사용자 펫의 이름을 변경합니다.")
    @PutMapping("/pets/name")
    fun updateUserPetName(
        user: User,
        @RequestBody
        request: UpdateUserPetNameRequest,
    ): UserPetInfoResponse {
        val updatedPetInfo = userPetService.updatePetName(user.id, request.nickname)
        val petLevel = userPetPolicy.getLevelType(updatedPetInfo.point)
        val maxLevelStandard = userPetPolicy.getMaxLevelStandard(petLevel)
        return UserPetInfoResponse.of(updatedPetInfo, petLevel, maxLevelStandard)
    }

    @Operation(summary = "현 시즌 펫 성장 기록 조회", description = "사용자의 현 시즌 펫 성장 기록을 조회합니다.")
    @GetMapping("/pets/season")
    fun findUserPetSeasonInfo(user: User): UserPetLevelHistoryAllResponse {
        val histories = userPetFacade.findCurrentSeasonPetHistory(user.id)
        return UserPetLevelHistoryAllResponse.of(histories)
    }
}
