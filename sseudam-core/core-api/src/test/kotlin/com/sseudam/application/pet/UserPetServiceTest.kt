package com.sseudam.application.pet

import com.sseudam.DevelopTest
import com.sseudam.fixture.pet.PetFixture
import com.sseudam.pet.PetPointAction
import com.sseudam.pet.UserPet
import com.sseudam.pet.UserPetService
import com.sseudam.pet.component.UserPetAppender
import com.sseudam.pet.component.UserPetDeleter
import com.sseudam.pet.component.UserPetReader
import com.sseudam.pet.component.UserPetUpdater
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify

@DevelopTest
class UserPetServiceTest :
    DescribeSpec({
        val userPetAppender: UserPetAppender = mockk()
        val userPetReader: UserPetReader = mockk()
        val userPetUpdater: UserPetUpdater = mockk()
        val userPetDeleter: UserPetDeleter = mockk()
        val userPetService = UserPetService(userPetAppender, userPetReader, userPetUpdater, userPetDeleter)

        describe("사용자별 펫 조회") {
            context("사용자의 펫이 존재하는 경우") {
                it("펫 정보를 반환한다") {
                    val userId = 1L
                    val userPetInfo = PetFixture.userPetInfo

                    every { userPetReader.readPetInfoByUser(userId) } returns userPetInfo

                    val result = userPetService.findByUser(userId)

                    result shouldBe userPetInfo
                    verify { userPetReader.readPetInfoByUser(userId) }
                }
            }

            context("사용자의 펫이 없는 경우") {
                it("null을 반환한다") {
                    val userId = 999L

                    every { userPetReader.readPetInfoByUser(userId) } returns null

                    val result = userPetService.findByUser(userId)

                    result shouldBe null
                    verify { userPetReader.readPetInfoByUser(userId) }
                }
            }
        }

        describe("전체 펫 목록 조회") {
            context("펫이 존재하는 경우") {
                it("모든 펫 목록을 반환한다") {
                    val userPets = PetFixture.userPetInfos

                    every { userPetReader.readAll() } returns userPets

                    val result = userPetService.findAll()

                    result shouldBe userPets
                    result shouldHaveSize 2
                    verify { userPetReader.readAll() }
                }
            }

            context("펫이 없는 경우") {
                it("빈 목록을 반환한다") {
                    every { userPetReader.readAll() } returns emptyList()

                    val result = userPetService.findAll()

                    result.shouldBeEmpty()
                    verify { userPetReader.readAll() }
                }
            }
        }

        describe("펫 추가") {
            context("유효한 사용자와 펫 정보로 추가하는 경우") {
                it("새로운 펫을 생성하고 반환한다") {
                    val userId = 1L
                    val petInfo = PetFixture.petInfo
                    val createdUserPet = PetFixture.userPetInfo

                    every { userPetAppender.append(userId, petInfo) } returns createdUserPet

                    val result = userPetService.append(userId, petInfo)

                    result shouldBe createdUserPet
                    verify { userPetAppender.append(userId, petInfo) }
                }
            }
        }

        describe("펫 포인트 업데이트") {
            context("방문 인증 액션으로 포인트를 업데이트하는 경우") {
                it("포인트가 증가된 펫 정보를 반환한다") {
                    val userPet = PetFixture.userPetInfo.copy(point = 100L)
                    val action = PetPointAction.SPOT_VISITED
                    val updatedUserPet = userPet.copy(point = 105L)

                    every { userPetUpdater.updatePointByAction(userPet.id, action) } returns updatedUserPet

                    val result = userPetService.updatePointByAction(userPet, action)

                    result shouldBe updatedUserPet
                    result.point shouldBe 105L
                    verify { userPetUpdater.updatePointByAction(userPet.id, action) }
                }
            }

            context("신고 승인 액션으로 포인트를 업데이트하는 경우") {
                it("더 많은 포인트가 증가된다") {
                    val userPet = PetFixture.userPetInfo.copy(point = 100L)
                    val action = PetPointAction.REPORT_APPROVED
                    val updatedUserPet = userPet.copy(point = 115L)

                    every { userPetUpdater.updatePointByAction(userPet.id, action) } returns updatedUserPet

                    val result = userPetService.updatePointByAction(userPet, action)

                    result.point shouldBe 115L
                    verify { userPetUpdater.updatePointByAction(userPet.id, action) }
                }
            }

            context("출석 체크 액션으로 포인트를 업데이트하는 경우") {
                it("포인트가 증가된다") {
                    val userPet = PetFixture.userPetInfo.copy(point = 50L)
                    val action = PetPointAction.ATTENDANCE
                    val updatedUserPet = userPet.copy(point = 52L)

                    every { userPetUpdater.updatePointByAction(userPet.id, action) } returns updatedUserPet

                    val result = userPetService.updatePointByAction(userPet, action)

                    result.point shouldBe 52L
                }
            }
        }

        describe("펫 이름 변경") {
            context("유효한 닉네임으로 변경하는 경우") {
                it("변경된 펫 정보를 반환한다") {
                    val userId = 1L
                    val newNickname = "새냥이"
                    val updatedUserPet = PetFixture.userPetInfo.copy(nickname = newNickname)

                    every { userPetUpdater.updatePetName(userId, newNickname) } returns updatedUserPet

                    val result = userPetService.updatePetName(userId, newNickname)

                    result shouldBe updatedUserPet
                    result.nickname shouldBe newNickname
                    verify { userPetUpdater.updatePetName(userId, newNickname) }
                }
            }
        }

        describe("펫 ID 변경") {
            context("새로운 펫 ID로 변경하는 경우") {
                it("변경된 펫 정보를 반환한다") {
                    val userPetId = 1L
                    val newPetId = 2L
                    val updatedUserPet = PetFixture.userPetInfo.copy(petId = newPetId)

                    every { userPetUpdater.updatePetId(userPetId, newPetId) } returns updatedUserPet

                    val result = userPetService.updatePetId(userPetId, newPetId)

                    result shouldBe updatedUserPet
                    result.petId shouldBe newPetId
                    verify { userPetUpdater.updatePetId(userPetId, newPetId) }
                }
            }
        }

        describe("모든 사용자 펫 포인트 초기화") {
            context("새로운 시즌을 시작하는 경우") {
                it("기존 펫을 삭제하고 새로 생성한다") {
                    val userPets = PetFixture.userPetInfos
                    val newPetId = 10L
                    val userIds = userPets.map { it.userId }

                    every { userPetDeleter.deleteAllByUserIds(userIds) } just Runs
                    every { userPetAppender.appendAll(userPets, newPetId) } just Runs

                    userPetService.initPointForAllUsers(userPets, newPetId)

                    verify { userPetDeleter.deleteAllByUserIds(userIds) }
                    verify { userPetAppender.appendAll(userPets, newPetId) }
                }
            }

            context("사용자가 없는 경우") {
                it("삭제와 생성을 스킨한다") {
                    val emptyUserPets = emptyList<UserPet.Info>()
                    val newPetId = 10L

                    every { userPetDeleter.deleteAllByUserIds(emptyList()) } just Runs
                    every { userPetAppender.appendAll(emptyUserPets, newPetId) } just Runs

                    userPetService.initPointForAllUsers(emptyUserPets, newPetId)

                    verify { userPetDeleter.deleteAllByUserIds(emptyList()) }
                    verify { userPetAppender.appendAll(emptyUserPets, newPetId) }
                }
            }
        }

        describe("사용자 펫 삭제") {
            context("사용자 ID로 삭제하는 경우") {
                it("해당 사용자의 펫을 삭제한다") {
                    val userId = 1L

                    every { userPetDeleter.deleteByUser(userId) } just Runs

                    userPetService.deleteByUser(userId)

                    verify { userPetDeleter.deleteByUser(userId) }
                }
            }
        }
    })
