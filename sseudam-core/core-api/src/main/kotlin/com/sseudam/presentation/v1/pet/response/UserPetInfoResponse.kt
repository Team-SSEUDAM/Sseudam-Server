package com.sseudam.presentation.v1.pet.response

import com.sseudam.pet.Pet
import com.sseudam.pet.UserPet
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "사용자 펫 응답 Json")
data class UserPetInfoResponse(
    @Schema(description = "사용자 ID", example = "1")
    val userId: Long,
    @Schema(description = "펫 ID", example = "1")
    val petId: Long,
    @Schema(description = "펫 닉네임", example = "사용자 펫 닉네임")
    val nickname: String,
    @Schema(description = "펫 포인트", example = "235")
    val point: Long,
    @Schema(description = "레벨 타입", example = "LEVEL_1")
    val levelType: Pet.LevelType,
    @Schema(description = "레벨 최대 포인트 기준", example = "20")
    val maxLevelStandard: Long,
    @Schema(description = "현 시즌", example = "2025-07")
    val season: String,
    @Schema(description = "생성 일자", example = "2025-07-05T12:00:00")
    val createdAt: LocalDateTime,
) {
    companion object {
        fun of(
            userPetInfo: UserPet.Info,
            levelType: Pet.LevelType,
            season: String,
            maxLevelStandard: Long,
        ): UserPetInfoResponse =
            UserPetInfoResponse(
                userId = userPetInfo.userId,
                petId = userPetInfo.petId,
                nickname = levelType.adjective + userPetInfo.nickname,
                point = userPetInfo.point,
                levelType = levelType,
                maxLevelStandard = maxLevelStandard,
                season = season,
                createdAt = userPetInfo.createdAt,
            )
    }
}
