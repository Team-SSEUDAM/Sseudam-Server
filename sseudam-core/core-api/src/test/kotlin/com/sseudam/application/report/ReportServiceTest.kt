package com.sseudam.application.report

import com.sseudam.DevelopTest
import com.sseudam.fixture.common.PageFixture
import com.sseudam.fixture.report.ReportFixture
import com.sseudam.report.ReportService
import com.sseudam.report.ReportStatus
import com.sseudam.report.ReportType
import com.sseudam.report.SpotReport
import com.sseudam.report.component.ReportAppender
import com.sseudam.report.component.ReportReader
import com.sseudam.report.component.ReportUpdater
import com.sseudam.report.component.ReportValidator
import com.sseudam.report.event.SpotReportUpdateEvent
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
import org.springframework.context.ApplicationEventPublisher

@DevelopTest
class ReportServiceTest :
    DescribeSpec({
        val reportAppender: ReportAppender = mockk()
        val reportReader: ReportReader = mockk()
        val reportUpdater: ReportUpdater = mockk()
        val reportValidator: ReportValidator = mockk()
        val applicationEventPublisher: ApplicationEventPublisher = mockk()

        val reportService = ReportService(reportAppender, reportReader, reportUpdater, reportValidator, applicationEventPublisher)

        describe("신고 추가") {
            context("유효한 신고 정보인 경우") {
                it("신고를 생성하고 결과를 반환한다") {
                    val imageUrl = "https://example.com/image.jpg"
                    val create = ReportFixture.spotReportCreate
                    val reportInfo = ReportFixture.spotReportInfo

                    every { reportAppender.append(imageUrl, create) } returns reportInfo

                    val result = reportService.appendReport(imageUrl, create)

                    result shouldBe reportInfo
                    verify { reportAppender.append(imageUrl, create) }
                }
            }
        }

        describe("사용자별 신고 내역 조회") {
            context("신고 내역이 있는 경우") {
                it("신고 목록을 반환한다") {
                    val userId = 1L
                    val reports = listOf(ReportFixture.spotReportInfo)

                    every { reportReader.readAllByUserId(userId) } returns reports

                    val result = reportService.findAllReportByUserId(userId)

                    result shouldBe reports
                    verify { reportReader.readAllByUserId(userId) }
                }
            }

            context("신고 내역이 없는 경우") {
                it("빈 목록을 반환한다") {
                    val userId = 999L

                    every { reportReader.readAllByUserId(userId) } returns emptyList()

                    val result = reportService.findAllReportByUserId(userId)

                    result shouldBe emptyList()
                }
            }
        }

        describe("사용자별 신고 상세 내역 조회") {
            context("신고 상세 내역이 있는 경우") {
                it("신고 상세 목록을 반환한다") {
                    val userId = 1L
                    val reportDetails = listOf(ReportFixture.spotReportDetail)

                    every { reportReader.readAllDetailByUserId(userId) } returns reportDetails

                    val result = reportService.findAllDetailsByUserId(userId)

                    result shouldBe reportDetails
                    verify { reportReader.readAllDetailByUserId(userId) }
                }
            }

            context("신고 상세 내역이 없는 경우") {
                it("빈 목록을 반환한다") {
                    val userId = 999L

                    every { reportReader.readAllDetailByUserId(userId) } returns emptyList()

                    val result = reportService.findAllDetailsByUserId(userId)

                    result shouldBe emptyList()
                }
            }
        }

        describe("신고 목록 조회") {
            context("페이지 요청이 있는 경우") {
                it("페이징된 신고 목록을 반환한다") {
                    val offsetPageRequest = PageFixture.offsetPageRequest
                    val reportDetail: SpotReport.Detail = mockk()
                    val page = Page(listOf(reportDetail), 1)

                    every { reportReader.readAllBy(offsetPageRequest, null) } returns page

                    val result = reportService.findReportsBy(offsetPageRequest, null)

                    result shouldBe page
                    verify { reportReader.readAllBy(offsetPageRequest, null) }
                }
            }

            context("타입 필터가 있는 경우") {
                it("해당 타입의 신고 목록을 반환한다") {
                    val offsetPageRequest = PageFixture.offsetPageRequest
                    val reportType = ReportType.POINT
                    val reportDetail: SpotReport.Detail = mockk()
                    val page = Page(listOf(reportDetail), 1)

                    every { reportReader.readAllBy(offsetPageRequest, reportType) } returns page

                    val result = reportService.findReportsBy(offsetPageRequest, reportType)

                    result shouldBe page
                    verify { reportReader.readAllBy(offsetPageRequest, reportType) }
                }
            }
        }

        describe("신고 상세 조회") {
            context("존재하는 신고인 경우") {
                it("신고 정보를 반환한다") {
                    val reportId = 1L
                    val reportInfo = ReportFixture.spotReportInfo

                    every { reportReader.readBy(reportId) } returns reportInfo

                    val result = reportService.findSpotReportById(reportId)

                    result shouldBe reportInfo
                    verify { reportReader.readBy(reportId) }
                }
            }
        }

        describe("반려 신고 조회") {
            context("반려된 신고인 경우") {
                it("반려 정보를 반환한다") {
                    val reportId = 1L
                    val rejectInfo = ReportFixture.reportRejectInfo

                    every { reportReader.readRejectByReportId(reportId) } returns rejectInfo

                    val result = reportService.findRejectReportByReportId(reportId)

                    result shouldBe rejectInfo
                    verify { reportReader.readRejectByReportId(reportId) }
                }
            }

            context("반려되지 않은 신고인 경우") {
                it("null을 반환한다") {
                    val reportId = 1L

                    every { reportReader.readRejectByReportId(reportId) } returns null

                    val result = reportService.findRejectReportByReportId(reportId)

                    result shouldBe null
                    verify { reportReader.readRejectByReportId(reportId) }
                }
            }
        }

        describe("신고 상태 업데이트") {
            context("유효한 상태 변경인 경우") {
                it("상태를 업데이트하고 이벤트를 발행한다") {
                    val updateCommand = ReportFixture.updateReportCommand
                    val reportInfo = ReportFixture.spotReportInfo

                    every { reportUpdater.update(updateCommand.reportId, updateCommand.status) } returns reportInfo
                    every { applicationEventPublisher.publishEvent(any<SpotReportUpdateEvent>()) } just Runs

                    val result = reportService.updateSpotReport(updateCommand)

                    result shouldBe reportInfo
                    verify { reportUpdater.update(updateCommand.reportId, updateCommand.status) }
                    verify { applicationEventPublisher.publishEvent(any<SpotReportUpdateEvent>()) }
                }
            }

            context("거절 사유와 함께 상태 변경하는 경우") {
                it("상태를 업데이트하고 사유와 함께 이벤트를 발행한다") {
                    val updateCommand = ReportFixture.updateReportCommand.copy(status = ReportStatus.REJECT, reason = "부적절한 위치")
                    val reportInfo = ReportFixture.spotReportInfo

                    every { reportUpdater.update(updateCommand.reportId, updateCommand.status) } returns reportInfo
                    every { applicationEventPublisher.publishEvent(any<SpotReportUpdateEvent>()) } just Runs

                    val result = reportService.updateSpotReport(updateCommand)

                    result shouldBe reportInfo
                    verify { reportUpdater.update(updateCommand.reportId, updateCommand.status) }
                    verify { applicationEventPublisher.publishEvent(SpotReportUpdateEvent(reportInfo, updateCommand.reason)) }
                }
            }
        }

        describe("신고 이름 검증") {
            context("중복되지 않은 이름인 경우") {
                it("예외를 던지지 않는다") {
                    val name = "새로운 쓰레기통"

                    every { reportReader.existsByName(name) } returns false

                    reportService.validateSpotReportName(name)

                    verify { reportReader.existsByName(name) }
                }
            }

            context("중복된 이름인 경우") {
                it("예외를 던진다") {
                    val name = "기존 쓰레기통"

                    every { reportReader.existsByName(name) } returns true

                    val exception =
                        shouldThrow<ErrorException> {
                            reportService.validateSpotReportName(name)
                        }

                    exception.errorType shouldBe ErrorType.DUPLICATE_SPOT_NAME
                    verify { reportReader.existsByName(name) }
                }
            }
        }

        describe("신고 취소") {
            context("본인의 신고를 취소하는 경우") {
                it("신고 상태를 취소로 변경한다") {
                    val command = ReportFixture.cancelReportCommand
                    val reportInfo = ReportFixture.spotReportInfo

                    every { reportReader.readBy(command.reportId) } returns reportInfo
                    every { reportValidator.verifyReport(command.userId, reportInfo) } just Runs
                    every { reportUpdater.cancel(command.reportId, ReportStatus.CANCEL) } just Runs

                    reportService.cancel(command)

                    verify { reportReader.readBy(command.reportId) }
                    verify { reportValidator.verifyReport(command.userId, reportInfo) }
                    verify { reportUpdater.cancel(command.reportId, ReportStatus.CANCEL) }
                }
            }

            context("다른 사용자의 신고를 취소하려는 경우") {
                it("예외가 발생한다") {
                    val command = ReportFixture.cancelReportCommand
                    val reportInfo = ReportFixture.spotReportInfo.copy(userId = 2L)

                    every { reportReader.readBy(command.reportId) } returns reportInfo
                    every { reportValidator.verifyReport(command.userId, reportInfo) } throws ErrorException(ErrorType.UNAUTHORIZED_REPORT)

                    shouldThrow<ErrorException> {
                        reportService.cancel(command)
                    }

                    verify { reportReader.readBy(command.reportId) }
                    verify { reportValidator.verifyReport(command.userId, reportInfo) }
                }
            }
        }
    })
