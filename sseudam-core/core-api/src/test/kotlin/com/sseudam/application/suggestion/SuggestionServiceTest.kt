package com.sseudam.application.suggestion

import com.sseudam.DevelopTest
import com.sseudam.fixture.common.PageFixture
import com.sseudam.fixture.suggestion.SuggestionFixture
import com.sseudam.suggestion.SpotSuggestion
import com.sseudam.suggestion.SuggestionService
import com.sseudam.suggestion.SuggestionStatus
import com.sseudam.suggestion.component.SuggestionAppender
import com.sseudam.suggestion.component.SuggestionReader
import com.sseudam.suggestion.component.SuggestionUpdater
import com.sseudam.suggestion.component.SuggestionValidator
import com.sseudam.suggestion.event.SuggestionUpdateEvent
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import com.sseudam.support.page.Page
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.locationtech.jts.geom.Point
import org.springframework.context.ApplicationEventPublisher

@DevelopTest
class SuggestionServiceTest :
    DescribeSpec({
        val suggestionAppender: SuggestionAppender = mockk()
        val suggestionReader: SuggestionReader = mockk()
        val suggestionValidator: SuggestionValidator = mockk()
        val suggestionUpdater: SuggestionUpdater = mockk()
        val applicationEventPublisher: ApplicationEventPublisher = mockk()
        val suggestionService =
            SuggestionService(
                suggestionAppender = suggestionAppender,
                suggestionReader = suggestionReader,
                suggestionValidator = suggestionValidator,
                suggestionUpdater = suggestionUpdater,
                applicationEventPublisher = applicationEventPublisher,
            )

        describe("제보 추가") {
            context("유효한 제보 정보인 경우") {
                it("제보를 생성하고 결과를 반환한다") {
                    val create = SuggestionFixture.spotSuggestionCreateRequest.toCommand(1L)
                    val uploadUrl = SuggestionFixture.s3ImageUrl
                    val suggestionInfo = SuggestionFixture.spotSuggestionInfo

                    every { suggestionValidator.verifyPoint(any()) } just Runs
                    every { suggestionAppender.append(uploadUrl.imageUrl, create) } returns suggestionInfo

                    val result = suggestionService.append(create, uploadUrl)

                    result.suggestionInfo shouldBe suggestionInfo
                    result.uploadUrl shouldBe uploadUrl
                    verify { suggestionValidator.verifyPoint(any()) }
                    verify { suggestionAppender.append(uploadUrl.imageUrl, create) }
                }
            }

            context("중복된 위치에 제보하는 경우") {
                it("예외가 발생한다") {
                    val create = SuggestionFixture.spotSuggestionCreateRequest.toCommand(1L)
                    val uploadUrl = SuggestionFixture.s3ImageUrl

                    every { suggestionValidator.verifyPoint(any()) } throws ErrorException(ErrorType.ALREADY_EXIST_SPOT_POINT)

                    shouldThrow<ErrorException> {
                        suggestionService.append(create, uploadUrl)
                    }

                    verify { suggestionValidator.verifyPoint(any()) }
                }
            }
        }

        describe("사용자별 제보 내역 조회") {
            context("제보 내역이 있는 경우") {
                it("제보 목록을 반환한다") {
                    val userId = 1L
                    val suggestions = listOf(SuggestionFixture.spotSuggestionInfo)

                    every { suggestionReader.readAllByUser(userId) } returns suggestions

                    val result = suggestionService.findAllSpotSuggestionByUser(userId)

                    result shouldBe suggestions
                    verify { suggestionReader.readAllByUser(userId) }
                }
            }

            context("제보 내역이 없는 경우") {
                it("빈 목록을 반환한다") {
                    val userId = 999L

                    every { suggestionReader.readAllByUser(userId) } returns emptyList()

                    val result = suggestionService.findAllSpotSuggestionByUser(userId)

                    result shouldBe emptyList()
                }
            }
        }

        describe("위치로 제보 조회") {
            context("해당 위치에 제보가 있는 경우") {
                it("제보 정보를 반환한다") {
                    val point: Point = mockk()
                    val suggestionInfo = SuggestionFixture.spotSuggestionInfo

                    every { suggestionReader.readByPoint(point) } returns suggestionInfo

                    val result = suggestionService.findSpotSuggestionByPoint(point)

                    result shouldBe suggestionInfo
                }
            }

            context("해당 위치에 제보가 없는 경우") {
                it("null을 반환한다") {
                    val point: Point = mockk()

                    every { suggestionReader.readByPoint(point) } returns null

                    val result = suggestionService.findSpotSuggestionByPoint(point)

                    result shouldBe null
                }
            }
        }

        describe("제보 목록 조회") {
            context("페이지 요청이 있는 경우") {
                it("페이징된 제보 목록을 반환한다") {
                    val offsetPageRequest = PageFixture.offsetPageRequest
                    val suggestionDetail: SpotSuggestion.Detail = mockk()
                    val page = Page(listOf(suggestionDetail), 1)

                    every { suggestionReader.readAllBy(offsetPageRequest, null) } returns page

                    val result = suggestionService.findSuggestionsBy(offsetPageRequest, null)

                    result shouldBe page
                    verify { suggestionReader.readAllBy(offsetPageRequest, null) }
                }
            }

            context("상태 필터가 있는 경우") {
                it("해당 상태의 제보 목록을 반환한다") {
                    val offsetPageRequest = PageFixture.offsetPageRequest
                    val status = SuggestionStatus.WAITING
                    val suggestionDetail: SpotSuggestion.Detail = mockk()
                    val page = Page(listOf(suggestionDetail), 1)

                    every { suggestionReader.readAllBy(offsetPageRequest, status) } returns page

                    val result = suggestionService.findSuggestionsBy(offsetPageRequest, status)

                    result shouldBe page
                    verify { suggestionReader.readAllBy(offsetPageRequest, status) }
                }
            }
        }

        describe("제보 상세 조회") {
            context("존재하는 제보인 경우") {
                it("제보 상세 정보를 반환한다") {
                    val suggestionId = 1L
                    val suggestionInfo = SuggestionFixture.spotSuggestionInfo

                    every { suggestionReader.readBy(suggestionId) } returns suggestionInfo
                    every { suggestionReader.readRejectBySuggestionId(suggestionId) } returns null

                    val result = suggestionService.findSpotSuggestionById(suggestionId)

                    result shouldBe SpotSuggestion.Detail.of(suggestionInfo, null)
                    verify { suggestionReader.readBy(suggestionId) }
                    verify { suggestionReader.readRejectBySuggestionId(suggestionId) }
                }
            }
        }

        describe("제보 상태 업데이트") {
            context("유효한 상태 변경인 경우") {
                it("상태를 업데이트하고 이벤트를 발행한다") {
                    val suggestionId = 1L
                    val status = SuggestionStatus.APPROVE
                    val reason: String? = null
                    val suggestionInfo = SuggestionFixture.spotSuggestionInfo

                    every { suggestionUpdater.update(suggestionId, status) } returns suggestionInfo
                    every { applicationEventPublisher.publishEvent(any<SuggestionUpdateEvent>()) } just Runs

                    val result = suggestionService.updateStatus(suggestionId, status, reason)

                    result shouldBe suggestionInfo
                    verify { suggestionUpdater.update(suggestionId, status) }
                    verify { applicationEventPublisher.publishEvent(any<SuggestionUpdateEvent>()) }
                }
            }

            context("거절 사유와 함께 상태 변경하는 경우") {
                it("상태를 업데이트하고 사유와 함께 이벤트를 발행한다") {
                    val suggestionId = 1L
                    val status = SuggestionStatus.REJECT
                    val reason = "부적절한 위치"
                    val suggestionInfo = SuggestionFixture.spotSuggestionInfo

                    every { suggestionUpdater.update(suggestionId, status) } returns suggestionInfo
                    every { applicationEventPublisher.publishEvent(any<SuggestionUpdateEvent>()) } just Runs

                    val result = suggestionService.updateStatus(suggestionId, status, reason)

                    result shouldBe suggestionInfo
                    verify { suggestionUpdater.update(suggestionId, status) }
                    verify { applicationEventPublisher.publishEvent(SuggestionUpdateEvent(suggestionInfo, reason)) }
                }
            }
        }

        describe("제보 이름 검증") {
            context("중복되지 않은 이름인 경우") {
                it("예외를 던지지 않는다") {
                    val name = "새로운 쓰레기통"

                    every { suggestionReader.existsByName(name) } returns false

                    suggestionService.validateSpotSuggestionName(name)

                    verify { suggestionReader.existsByName(name) }
                }
            }

            context("중복된 이름인 경우") {
                it("예외를 던진다") {
                    val name = "기존 쓰레기통"

                    every { suggestionReader.existsByName(name) } returns true

                    val exception =
                        shouldThrow<ErrorException> {
                            suggestionService.validateSpotSuggestionName(name)
                        }

                    exception.errorType shouldBe ErrorType.DUPLICATE_SPOT_NAME
                    verify { suggestionReader.existsByName(name) }
                }
            }
        }

        describe("제보 거절 추가") {
            context("유효한 거절 정보인 경우") {
                it("거절 정보를 추가한다") {
                    val suggestionId = 1L
                    val reason = "부적절한 위치"

                    every { suggestionAppender.appendReject(suggestionId, reason) } just Runs

                    suggestionService.appendReject(suggestionId, reason)

                    verify { suggestionAppender.appendReject(suggestionId, reason) }
                }
            }

            context("거절 사유가 없는 경우") {
                it("사유 없이 거절 정보를 추가한다") {
                    val suggestionId = 1L

                    every { suggestionAppender.appendReject(suggestionId, null) } just Runs

                    suggestionService.appendReject(suggestionId, null)

                    verify { suggestionAppender.appendReject(suggestionId, null) }
                }
            }
        }

        describe("제보 취소") {
            context("본인의 제보를 취소하는 경우") {
                it("제보 상태를 취소로 변경한다") {
                    val command = SuggestionFixture.cancelSuggestionCommand
                    val suggestionInfo = SuggestionFixture.spotSuggestionInfo

                    every { suggestionReader.readBy(command.suggestionId) } returns suggestionInfo
                    every { suggestionValidator.verifySuggestion(command.userId, suggestionInfo) } just Runs
                    every { suggestionUpdater.cancel(command.suggestionId) } just Runs

                    suggestionService.cancel(command)

                    verify { suggestionReader.readBy(command.suggestionId) }
                    verify { suggestionValidator.verifySuggestion(command.userId, suggestionInfo) }
                    verify { suggestionUpdater.cancel(command.suggestionId) }
                }
            }

            context("다른 사용자의 제보를 취소하려는 경우") {
                it("예외가 발생한다") {
                    val command = SuggestionFixture.cancelSuggestionCommand
                    val suggestionInfo = SuggestionFixture.spotSuggestionInfo.copy(userId = command.userId + 1)

                    every { suggestionReader.readBy(command.suggestionId) } returns suggestionInfo
                    every { suggestionValidator.verifySuggestion(command.userId, suggestionInfo) } throws ErrorException(ErrorType.UNAUTHORIZED_SUGGESTION)

                    shouldThrow<ErrorException> {
                        suggestionService.cancel(command)
                    }

                    verify { suggestionReader.readBy(command.suggestionId) }
                    verify { suggestionValidator.verifySuggestion(command.userId, suggestionInfo) }
                    verify { suggestionUpdater.cancel(command.suggestionId) }
                }
            }
        }
    })
