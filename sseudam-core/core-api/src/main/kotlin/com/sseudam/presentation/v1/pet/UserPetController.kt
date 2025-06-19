package com.sseudam.presentation.v1.pet

import com.sseudam.pet.UserPetPolicy
import com.sseudam.pet.UserPetService
import com.sseudam.presentation.v1.annotation.ApiV1Controller
import com.sseudam.presentation.v1.pet.response.UserPetInfoResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping

@Tag(name = "😽 Pet API", description = "펫 관련 API")
@ApiV1Controller
class UserPetController(
    private val userPetService: UserPetService,
    private val userPetPolicy: UserPetPolicy,
) {
    @Operation(summary = "펫 정보 조회", description = "사용자의 펫 정보를 조회합니다.")
    @GetMapping("/pets")
    fun findUserPetInfo(userId: Long): UserPetInfoResponse {
        val petInfo = userPetService.findPetInfo(userId)
        val petLevel = userPetPolicy.getLevelType(petInfo.point)
        val maxLevelStandard = userPetPolicy.getMaxLevelStandard(petLevel)
        return UserPetInfoResponse.of(petInfo, petLevel, maxLevelStandard)
    }
}
