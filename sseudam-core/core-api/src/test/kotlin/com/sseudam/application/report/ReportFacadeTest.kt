package com.sseudam.application.report

import com.sseudam.DevelopTest
import com.sseudam.common.ImageS3Caller
import com.sseudam.fixture.report.ReportFixture
import com.sseudam.pet.PetPointAction
import com.sseudam.report.ReportFacade
import com.sseudam.report.ReportService
import com.sseudam.report.ReportType
import com.sseudam.report.event.SpotReportCreatedEvent
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import com.sseudam.trashspot.TrashSpotService
import com.sseudam.trashspot.image.TrashSpotImageService
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
class ReportFacadeTest :
    DescribeSpec({
        val reportService: ReportService = mockk()
        val trashSpotService: TrashSpotService = mockk()
        val trashSpotImageService: TrashSpotImageService = mockk()
        val imageS3Caller: ImageS3Caller = mockk()
        val applicationEventPublisher: ApplicationEventPublisher = mockk()
        val reportFacade =
            ReportFacade(
                reportService = reportService,
                trashSpotService = trashSpotService,
                trashSpotImageService = trashSpotImageService,
                imageS3Caller = imageS3Caller,
                applicationEventPublisher = applicationEventPublisher,
            )

        val imagePrefix = "report"

        describe("신고 검증") {
            context("중복되지 않은 이름인 경우") {
                it("신고 이름과 쓰레기통 이름을 검증하고 true를 반환한다") {
                    val name = "새로운 쓰레기통"

                    every { reportService.validateSpotReportName(name) } just Runs
                    every { trashSpotService.validateSpotName(name) } just Runs

                    val result = reportFacade.validateSpotReport(name)

                    result shouldBe true
                    verify { reportService.validateSpotReportName(name) }
                    verify { trashSpotService.validateSpotName(name) }
                }
            }

            context("신고에 중복된 이름이 있는 경우") {
                it("예외가 발생한다") {
                    val name = "기존 신고"

                    every { reportService.validateSpotReportName(name) } throws ErrorException(ErrorType.DUPLICATE_SPOT_NAME)

                    shouldThrow<ErrorException> {
                        reportFacade.validateSpotReport(name)
                    }

                    verify { reportService.validateSpotReportName(name) }
                }
            }

            context("쓰레기통에 중복된 이름이 있는 경우") {
                it("예외가 발생한다") {
                    val name = "기존 쓰레기통"

                    every { reportService.validateSpotReportName(name) } just Runs
                    every { trashSpotService.validateSpotName(name) } throws ErrorException(ErrorType.DUPLICATE_SPOT_NAME)

                    shouldThrow<ErrorException> {
                        reportFacade.validateSpotReport(name)
                    }

                    verify { reportService.validateSpotReportName(name) }
                    verify { trashSpotService.validateSpotName(name) }
                }
            }
        }

        describe("신고 상세 조회") {
            context("유효한 신고 ID인 경우") {
                it("신고 정보와 반려 정보를 조회하여 상세 정보를 반환한다") {
                    val reportId = 1L
                    val reportInfo = ReportFixture.spotReportInfo
                    val reportDetail = ReportFixture.spotReportDetail

                    every { reportService.findSpotReportById(reportId) } returns reportInfo
                    every { reportService.findRejectReportByReportId(reportId) } returns null

                    val result = reportFacade.findReportDetails(reportId)

                    result.id shouldBe reportDetail.id
                    verify { reportService.findSpotReportById(reportId) }
                    verify { reportService.findRejectReportByReportId(reportId) }
                }
            }
        }

        describe("신고 생성") {
            context("PHOTO 타입이 아닌 신고인 경우") {
                it("기존 이미지를 사용하여 신고를 생성하고 이벤트를 발행한다") {
                    val create = ReportFixture.spotReportCreate
                    val reportInfo = ReportFixture.spotReportInfo
                    val trashSpotImages = listOf(ReportFixture.trashSpotImageInfo)

                    every { trashSpotImageService.findBySpotId(create.spotId) } returns trashSpotImages
                    every { reportService.appendReport(any(), create) } returns reportInfo
                    every { applicationEventPublisher.publishEvent(any<SpotReportCreatedEvent>()) } just Runs

                    val result = reportFacade.createSpotReport(create)

                    result.spotReport shouldBe reportInfo
                    result.presignedUrl shouldBe null
                    verify { trashSpotImageService.findBySpotId(create.spotId) }
                    verify { reportService.appendReport(any(), create) }
                    verify { applicationEventPublisher.publishEvent(any<SpotReportCreatedEvent>()) }
                }
            }

            context("PHOTO 타입 신고인 경우") {
                it("새로운 이미지 URL을 생성하여 신고를 생성하고 이벤트를 발행한다") {
                    val create = ReportFixture.spotReportCreate.copy(reportType = ReportType.PHOTO)
                    val reportInfo = ReportFixture.spotReportInfo
                    val s3ImageUrl = ReportFixture.s3ImageUrl
                    val trashSpotImages = listOf(ReportFixture.trashSpotImageInfo)

                    every { trashSpotImageService.findBySpotId(create.spotId) } returns trashSpotImages
                    every { imageS3Caller.createUploadUrl(create.userId, "$imagePrefix/${create.spotId}") } returns s3ImageUrl
                    every { reportService.appendReport(s3ImageUrl.imageUrl, create) } returns reportInfo
                    every { applicationEventPublisher.publishEvent(any<SpotReportCreatedEvent>()) } just Runs

                    val result = reportFacade.createSpotReport(create)

                    result.spotReport shouldBe reportInfo
                    result.presignedUrl shouldBe s3ImageUrl.presignedUrl
                    verify { trashSpotImageService.findBySpotId(create.spotId) }
                    verify { imageS3Caller.createUploadUrl(create.userId, "$imagePrefix/${create.spotId}") }
                    verify { reportService.appendReport(s3ImageUrl.imageUrl, create) }
                    verify { applicationEventPublisher.publishEvent(any<SpotReportCreatedEvent>()) }
                }
            }

            context("쓰레기통 이미지가 없는 경우") {
                it("기본 이미지 URL을 사용하여 신고를 생성한다") {
                    val create = ReportFixture.spotReportCreate
                    val reportInfo = ReportFixture.spotReportInfo
                    val defaultImageUrl = "https://img.sseudam.me/dev/default_trash_profile.webp"

                    every { trashSpotImageService.findBySpotId(create.spotId) } returns emptyList()
                    every { reportService.appendReport(defaultImageUrl, create) } returns reportInfo
                    every { applicationEventPublisher.publishEvent(any<SpotReportCreatedEvent>()) } just Runs

                    val result = reportFacade.createSpotReport(create)

                    result.spotReport shouldBe reportInfo
                    result.presignedUrl shouldBe null
                    verify { trashSpotImageService.findBySpotId(create.spotId) }
                    verify { reportService.appendReport(defaultImageUrl, create) }
                }
            }

            context("이미지 업로드 URL 생성 실패하는 경우") {
                it("예외가 발생한다") {
                    val create = ReportFixture.spotReportCreate.copy(reportType = ReportType.PHOTO)
                    val trashSpotImages = listOf(ReportFixture.trashSpotImageInfo)

                    every { trashSpotImageService.findBySpotId(create.spotId) } returns trashSpotImages
                    every { imageS3Caller.createUploadUrl(create.userId, "$imagePrefix/${create.spotId}") } throws RuntimeException("S3 오류")

                    shouldThrow<RuntimeException> {
                        reportFacade.createSpotReport(create)
                    }

                    verify { trashSpotImageService.findBySpotId(create.spotId) }
                    verify { imageS3Caller.createUploadUrl(create.userId, "$imagePrefix/${create.spotId}") }
                }
            }

            context("신고 생성 중 예외가 발생하는 경우") {
                it("예외가 발생하고 이벤트가 발행되지 않는다") {
                    val create = ReportFixture.spotReportCreate
                    val trashSpotImages = listOf(ReportFixture.trashSpotImageInfo)

                    every { trashSpotImageService.findBySpotId(create.spotId) } returns trashSpotImages
                    every { reportService.appendReport(any(), create) } throws RuntimeException("신고 생성 실패")

                    shouldThrow<RuntimeException> {
                        reportFacade.createSpotReport(create)
                    }

                    verify { trashSpotImageService.findBySpotId(create.spotId) }
                    verify { reportService.appendReport(any(), create) }
                }
            }

            context("이벤트 발행 시 올바른 정보가 전달되는 경우") {
                it("userId, spotReport, petPointAction이 포함된 이벤트를 발행한다") {
                    val create = ReportFixture.spotReportCreate
                    val reportInfo = ReportFixture.spotReportInfo
                    val trashSpotImages = listOf(ReportFixture.trashSpotImageInfo)

                    every { trashSpotImageService.findBySpotId(create.spotId) } returns trashSpotImages
                    every { reportService.appendReport(any(), create) } returns reportInfo
                    every { applicationEventPublisher.publishEvent(any<SpotReportCreatedEvent>()) } just Runs

                    reportFacade.createSpotReport(create)

                    verify {
                        applicationEventPublisher.publishEvent(
                            match<SpotReportCreatedEvent> {
                                it.userId == create.userId &&
                                    it.spotReport == reportInfo &&
                                    it.petPointAction == PetPointAction.REPORT
                            },
                        )
                    }
                }
            }
        }
    })
