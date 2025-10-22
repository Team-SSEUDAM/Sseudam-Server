package com.sseudam.presentation.v1.pet

import com.sseudam.RestDocsTest
import com.sseudam.config.UserArgumentResolver
import com.sseudam.docs.RestDocsTestSuite
import com.sseudam.docs.support.Authorization
import com.sseudam.docs.support.DocsTag
import com.sseudam.docs.support.ENUM
import com.sseudam.docs.support.MockMvcExtensions.makeDocument
import com.sseudam.docs.support.NUMBER
import com.sseudam.docs.support.RestDocsUtils.headers
import com.sseudam.docs.support.RestDocsUtils.requestBody
import com.sseudam.docs.support.RestDocsUtils.responseBody
import com.sseudam.docs.support.STRING
import com.sseudam.docs.support.headerType
import com.sseudam.docs.support.type
import com.sseudam.fixture.pet.PetFixture
import com.sseudam.pet.Pet
import com.sseudam.pet.PetService
import com.sseudam.pet.UserPetFacade
import com.sseudam.pet.UserPetService
import com.sseudam.pet.component.UserPetPolicy
import com.sseudam.pet.result.UserPetCurrentSeasonHistoryResult
import com.sseudam.user.User
import io.mockk.every
import io.mockk.mockk
import io.restassured.http.ContentType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import java.util.UUID

@RestDocsTest
class PetControllerTest : RestDocsTestSuite() {
    private lateinit var petController: PetController
    private lateinit var petService: PetService
    private lateinit var userPetService: UserPetService
    private lateinit var userPetFacade: UserPetFacade
    private lateinit var userPetPolicy: UserPetPolicy
    private lateinit var userArgumentResolver: UserArgumentResolver

    @BeforeEach
    fun setUpTest() {
        userArgumentResolver = mockk()
        petService = mockk()
        userPetService = mockk()
        userPetFacade = mockk()
        userPetPolicy = mockk()
        petController =
            PetController(
                petService,
                userPetService,
                userPetFacade,
                userPetPolicy,
            )
        mockMvcSpec = mockController(petController, userArgumentResolver)
        every { userArgumentResolver.supportsParameter(any()) } returns true
        every { userArgumentResolver.resolveArgument(any(), any(), any(), any()) } returns
            User(
                id = 1L,
                key = UUID.randomUUID().toString(),
            )
    }

    @DisplayName("펫 정보 조회 - 200")
    @Test
    fun t1() {
        val userPetInfo = PetFixture.userPetInfo
        val petInfo = PetFixture.petInfo
        val levelType = Pet.LevelType.LEVEL_4
        val maxLevelStandard = 300L
        val season = "2025-06"

        every { userPetFacade.findPetInfo(any()) } returns userPetInfo
        every { petService.findBy(any()) } returns petInfo
        every { userPetPolicy.getLevelType(userPetInfo.point) } returns levelType
        every { userPetPolicy.getMaxLevelStandard(levelType) } returns maxLevelStandard
        every { userPetPolicy.getSeasonByPetInfo(petInfo) } returns season

        val response =
            given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                .get("/api/v1/pets")
                .then()
                .status(HttpStatus.OK)
                .contentType(ContentType.JSON)

        response.makeDocument(
            "펫 정보 조회",
            DocsTag.PET,
            headers(
                "Authorization" headerType Authorization,
            ),
            "UserPetInfoResponse",
            responseBody(
                "userId" type NUMBER means "사용자 ID" example "1",
                "petId" type NUMBER means "펫 ID" example "1",
                "nickname" type STRING means "펫 닉네임" example "왕 커서 귀여운 냥이",
                "point" type NUMBER means "펫 포인트" example "235",
                "levelType" type ENUM(Pet.LevelType::class) means "레벨 타입" example "LEVEL_4",
                "maxLevelStandard" type NUMBER means "레벨 최대 포인트 기준" example "300",
                "season" type STRING means "현 시즌" example "2025-06",
                "createdAt" type STRING means "생성 일자" example "2025-06-01T00:00:00",
            ),
        )
    }

    @DisplayName("펫 이름 변경 - 200")
    @Test
    fun t2() {
        val userPetInfo = PetFixture.userPetInfo
        val petInfo = PetFixture.petInfo
        val levelType = Pet.LevelType.LEVEL_4
        val maxLevelStandard = 300L
        val season = "2025-06"

        every { userPetService.updatePetName(any(), any()) } returns userPetInfo
        every { petService.findBy(any()) } returns petInfo
        every { userPetPolicy.getLevelType(userPetInfo.point) } returns levelType
        every { userPetPolicy.getMaxLevelStandard(levelType) } returns maxLevelStandard
        every { userPetPolicy.getSeasonByPetInfo(petInfo) } returns season

        val response =
            given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                .contentType(ContentType.JSON)
                .body(mapOf("nickname" to "냐이"))
                .put("/api/v1/pets/name")
                .then()
                .status(HttpStatus.OK)

        response.makeDocument(
            "펫 이름 변경",
            DocsTag.PET,
            headers(
                "Authorization" headerType Authorization,
            ),
            "UpdateUserPetNameRequest",
            "UserPetInfoResponse",
            requestBody(
                "nickname" type STRING means "변경할 펫 닉네임" example "냐이",
            ),
            responseBody(
                "userId" type NUMBER means "사용자 ID" example "1",
                "petId" type NUMBER means "펫 ID" example "1",
                "nickname" type STRING means "펫 닉네임" example "왕 커서 귀여운 냐이",
                "point" type NUMBER means "펫 포인트" example "235",
                "levelType" type ENUM(Pet.LevelType::class) means "레벨 타입" example "LEVEL_4",
                "maxLevelStandard" type NUMBER means "레벨 최대 포인트 기준" example "300",
                "season" type STRING means "현 시즌" example "2025-06",
                "createdAt" type STRING means "생성 일자" example "2025-06-01T00:00:00",
            ),
        )
    }

    @DisplayName("현 시즌 펫 성장 기록 조회 - 200")
    @Test
    fun t3() {
        val userPetInfo = PetFixture.userPetInfo
        val petInfo = PetFixture.petInfo
        val levelType = Pet.LevelType.LEVEL_4
        val maxLevelStandard = 300L
        val season = "2025-06"
        val seasonHistory =
            UserPetCurrentSeasonHistoryResult(
                userPetInfo = userPetInfo,
                seasonHistories = PetFixture.currentSeasonHistories,
            )

        every { userPetFacade.findCurrentSeasonPetHistory(any()) } returns seasonHistory
        every { petService.findBy(any()) } returns petInfo
        every { userPetPolicy.getLevelType(userPetInfo.point) } returns levelType
        every { userPetPolicy.getMaxLevelStandard(levelType) } returns maxLevelStandard
        every { userPetPolicy.getSeasonByPetInfo(petInfo) } returns season

        val response =
            given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                .get("/api/v1/pets/season")
                .then()
                .status(HttpStatus.OK)

        response.makeDocument(
            "현 시즌 펫 성장 기록 조회",
            DocsTag.PET,
            headers(
                "Authorization" headerType Authorization,
            ),
            "UserPetLevelHistoryCurrentSeasonAllResponse",
            responseBody(
                "userPetInfo" type com.sseudam.docs.support.OBJECT means "사용자 펫 정보",
                "userPetInfo.userId" type NUMBER means "사용자 ID" example "1",
                "userPetInfo.petId" type NUMBER means "펫 ID" example "1",
                "userPetInfo.nickname" type STRING means "펫 닉네임" example "왕 커서 귀여운 냥이",
                "userPetInfo.point" type NUMBER means "펫 포인트" example "235",
                "userPetInfo.levelType" type ENUM(Pet.LevelType::class) means "레벨 타입" example "LEVEL_4",
                "userPetInfo.maxLevelStandard" type NUMBER means "레벨 최대 포인트 기준" example "300",
                "userPetInfo.season" type STRING means "현 시즌" example "2025-06",
                "userPetInfo.createdAt" type STRING means "생성 일자" example "2025-06-01T00:00:00",
                "list" type com.sseudam.docs.support.ARRAY means "성장 기록 목록",
                "list[].userId" type NUMBER means "사용자 ID" example "1",
                "list[].nickname" type STRING means "펫 닉네임" example "냥이",
                "list[].point" type NUMBER means "펫 포인트" example "50",
                "list[].levelType" type ENUM(Pet.LevelType::class) means "레벨 타입" example "LEVEL_2",
                "list[].isLocked" type com.sseudam.docs.support.BOOLEAN means "잠금 여부" example "false",
                "list[].season" type STRING means "시즌" example "2025-06",
                "list[].createdAt" type STRING means "생성 일자" example "2025-06-15T00:00:00",
            ),
        )
    }

    @DisplayName("사용자 전체 펫 성장 기록 조회 - 200")
    @Test
    fun t4() {
        every { userPetFacade.findAllPetHistory(any()) } returns PetFixture.levelUpHistories

        val response =
            given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                .get("/api/v1/pets/history")
                .then()
                .status(HttpStatus.OK)

        response.makeDocument(
            "사용자 전체 펫 성장 기록 조회",
            DocsTag.PET,
            headers(
                "Authorization" headerType Authorization,
            ),
            "UserPetLevelHistoryAllResponse",
            responseBody(
                "list" type com.sseudam.docs.support.ARRAY means "성장 기록 목록",
                "list[].userId" type NUMBER means "사용자 ID" example "1",
                "list[].nickname" type STRING means "펫 닉네임" example "냥이",
                "list[].point" type NUMBER means "펫 포인트" example "50",
                "list[].levelType" type ENUM(Pet.LevelType::class) means "레벨 타입" example "LEVEL_2",
                "list[].season" type STRING means "시즌" example "2025-06",
                "list[].createdAt" type STRING means "생성 일자" example "2025-06-15T00:00:00",
            ),
        )
    }
}
