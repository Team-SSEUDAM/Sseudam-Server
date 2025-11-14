package com.sseudam.application.suggestion

import com.sseudam.DevelopTest
import com.sseudam.common.ImageS3Caller
import com.sseudam.fixture.suggestion.SuggestionFixture
import com.sseudam.pet.PetPointAction
import com.sseudam.suggestion.SuggestionFacade
import com.sseudam.suggestion.SuggestionService
import com.sseudam.suggestion.event.SpotSuggestionCreatedEvent
import com.sseudam.suggestion.result.CreateSpotSuggestionResult
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import com.sseudam.trashspot.component.TrashSpotReader
import com.sseudam.trashspot.component.TrashSpotValidator
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.springframework.context.ApplicationEventPublisher

@DevelopTest
class SuggestionFacadeTest :
    DescribeSpec({
        val suggestionService: SuggestionService = mockk()
        val trashSpotReader: TrashSpotReader = mockk()
        val trashSpotValidator: TrashSpotValidator = mockk()
        val imageS3Caller: ImageS3Caller = mockk()
        val applicationEventPublisher: ApplicationEventPublisher = mockk()
        val suggestionFacade =
            SuggestionFacade(
                suggestionService = suggestionService,
                trashSpotReader = trashSpotReader,
                trashSpotValidator = trashSpotValidator,
                imageS3Caller = imageS3Caller,
                applicationEventPublisher = applicationEventPublisher,
            )

        val imagePrefix = "suggestion"

        describe("제보 검증") {
            context("중복되지 않은 이름인 경우") {
                it("제보 이름과 쓰레기통 이름을 검증하고 true를 반환한다") {
                    val name = "새로운 쓰레기통"

                    every { suggestionService.validateSpotSuggestionName(name) } just Runs
                    every { trashSpotReader.existsByName(name) } returns false

                    val result = suggestionFacade.validateSpotSuggestion(name)

                    result shouldBe true
                    verify { suggestionService.validateSpotSuggestionName(name) }
                    verify { trashSpotReader.existsByName(name) }
                }
            }

            context("제보에 중복된 이름이 있는 경우") {
                it("예외가 발생한다") {
                    val name = "기존 제보"

                    every { suggestionService.validateSpotSuggestionName(name) } throws ErrorException(ErrorType.DUPLICATE_SPOT_NAME)

                    shouldThrow<ErrorException> {
                        suggestionFacade.validateSpotSuggestion(name)
                    }

                    verify { suggestionService.validateSpotSuggestionName(name) }
                }
            }

            context("쓰레기통에 중복된 이름이 있는 경우") {
                it("예외가 발생한다") {
                    val name = "기존 쓰레기통"

                    every { suggestionService.validateSpotSuggestionName(name) } just Runs
                    every { trashSpotReader.existsByName(name) } returns true

                    shouldThrow<ErrorException> {
                        suggestionFacade.validateSpotSuggestion(name)
                    }

                    verify { suggestionService.validateSpotSuggestionName(name) }
                    verify { trashSpotReader.existsByName(name) }
                }
            }
        }

        describe("제보 생성") {
            context("유효한 제보 정보인 경우") {
                it("트랜잭션 내에서 위치 검증, 이미지 URL 생성, 제보 생성, 캐시 삭제, 이벤트 발행을 순차적으로 실행한다") {
                    val create = SuggestionFixture.spotSuggestionCreateRequest.toCommand(1L)
                    val uploadUrl = SuggestionFixture.s3ImageUrl
                    val suggestionInfo = SuggestionFixture.spotSuggestionInfo
                    val createResult = CreateSpotSuggestionResult(suggestionInfo, uploadUrl.imageUrl)

                    every { trashSpotValidator.verifyPoint(any()) } just Runs
                    every { imageS3Caller.createUploadUrl(create.userId, imagePrefix) } returns uploadUrl
                    every { suggestionService.append(create, uploadUrl) } returns createResult
                    every { applicationEventPublisher.publishEvent(any<SpotSuggestionCreatedEvent>()) } just Runs

                    val result = suggestionFacade.createSpotSuggestion(create)

                    result shouldBe createResult
                    verify { trashSpotValidator.verifyPoint(any()) }
                    verify { imageS3Caller.createUploadUrl(create.userId, imagePrefix) }
                    verify { suggestionService.append(create, uploadUrl) }
                    verify { applicationEventPublisher.publishEvent(any<SpotSuggestionCreatedEvent>()) }
                }
            }

            context("중복된 위치에 제보하는 경우") {
                it("예외가 발생한다") {
                    val create = SuggestionFixture.spotSuggestionCreateRequest.toCommand(1L)

                    every { trashSpotValidator.verifyPoint(any()) } throws ErrorException(ErrorType.ALREADY_EXIST_SPOT_POINT)

                    shouldThrow<ErrorException> {
                        suggestionFacade.createSpotSuggestion(create)
                    }

                    verify { trashSpotValidator.verifyPoint(any()) }
                }
            }

            context("이미지 업로드 URL 생성 실패하는 경우") {
                it("예외가 발생한다") {
                    val create = SuggestionFixture.spotSuggestionCreateRequest.toCommand(1L)

                    every { trashSpotValidator.verifyPoint(any()) } just Runs
                    every { imageS3Caller.createUploadUrl(create.userId, imagePrefix) } throws RuntimeException("S3 오류")

                    shouldThrow<RuntimeException> {
                        suggestionFacade.createSpotSuggestion(create)
                    }

                    verify { trashSpotValidator.verifyPoint(any()) }
                    verify { imageS3Caller.createUploadUrl(create.userId, imagePrefix) }
                }
            }

            context("제보 생성 중 예외가 발생하는 경우") {
                it("예외가 발생하고 이벤트가 발행되지 않는다") {
                    val create = SuggestionFixture.spotSuggestionCreateRequest.toCommand(1L)
                    val uploadUrl = SuggestionFixture.s3ImageUrl

                    every { trashSpotValidator.verifyPoint(any()) } just Runs
                    every { imageS3Caller.createUploadUrl(create.userId, imagePrefix) } returns uploadUrl
                    every { suggestionService.append(create, uploadUrl) } throws RuntimeException("제보 생성 실패")

                    shouldThrow<RuntimeException> {
                        suggestionFacade.createSpotSuggestion(create)
                    }

                    verify { trashSpotValidator.verifyPoint(any()) }
                    verify { imageS3Caller.createUploadUrl(create.userId, imagePrefix) }
                    verify { suggestionService.append(create, uploadUrl) }
                }
            }

            context("작업 순서 검증") {
                it("위치 검증 -> URL 생성 -> 제보 생성 -> 이벤트 발행 순서로 실행된다") {
                    val create = SuggestionFixture.spotSuggestionCreateRequest.toCommand(1L)
                    val uploadUrl = SuggestionFixture.s3ImageUrl
                    val suggestionInfo = SuggestionFixture.spotSuggestionInfo
                    val createResult = CreateSpotSuggestionResult(suggestionInfo, uploadUrl.imageUrl)
                    val callOrder = mutableListOf<String>()

                    every { trashSpotValidator.verifyPoint(any()) } answers { callOrder.add("verify") }
                    every { imageS3Caller.createUploadUrl(create.userId, imagePrefix) } answers {
                        callOrder.add("url")
                        uploadUrl
                    }
                    every { suggestionService.append(create, uploadUrl) } answers {
                        callOrder.add("append")
                        createResult
                    }
                    every { applicationEventPublisher.publishEvent(any<SpotSuggestionCreatedEvent>()) } answers {
                        callOrder.add("event")
                    }

                    suggestionFacade.createSpotSuggestion(create)

                    callOrder shouldBe listOf("verify", "url", "append", "event")
                }
            }

            context("이벤트 발행 시 올바른 정보가 전달되는 경우") {
                it("userId, spotSuggestion, petPointAction이 포함된 이벤트를 발행한다") {
                    val create = SuggestionFixture.spotSuggestionCreateRequest.toCommand(1L)
                    val uploadUrl = SuggestionFixture.s3ImageUrl
                    val suggestionInfo = SuggestionFixture.spotSuggestionInfo
                    val createResult = CreateSpotSuggestionResult(suggestionInfo, uploadUrl.imageUrl)

                    every { trashSpotValidator.verifyPoint(any()) } just Runs
                    every { imageS3Caller.createUploadUrl(create.userId, imagePrefix) } returns uploadUrl
                    every { suggestionService.append(create, uploadUrl) } returns createResult
                    every { applicationEventPublisher.publishEvent(any<SpotSuggestionCreatedEvent>()) } just Runs

                    suggestionFacade.createSpotSuggestion(create)

                    verify {
                        applicationEventPublisher.publishEvent(
                            match<SpotSuggestionCreatedEvent> {
                                it.userId == create.userId &&
                                    it.spotSuggestion == suggestionInfo &&
                                    it.petPointAction == PetPointAction.SUGGESTION
                            },
                        )
                    }
                }
            }
        }
    })
