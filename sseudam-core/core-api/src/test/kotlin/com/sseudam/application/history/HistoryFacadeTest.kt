package com.sseudam.application.history

import com.sseudam.DevelopTest
import com.sseudam.fixture.report.ReportFixture
import com.sseudam.fixture.suggestion.SuggestionFixture
import com.sseudam.history.HistoryFacade
import com.sseudam.history.dto.HistoryStatus
import com.sseudam.history.dto.SpotActionType
import com.sseudam.report.ReportService
import com.sseudam.report.ReportStatus
import com.sseudam.suggestion.SuggestionService
import com.sseudam.suggestion.SuggestionStatus
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

@DevelopTest
class HistoryFacadeTest :
    DescribeSpec({
        val reportService: ReportService = mockk()
        val suggestionService: SuggestionService = mockk()
        val historyFacade = HistoryFacade(reportService, suggestionService)

        describe("내역 조회") {
            context("신고와 제보 내역이 모두 있는 경우") {
                it("모든 내역을 생성일자 내림차순으로 반환한다") {
                    val userId = 1L
                    val reports = listOf(ReportFixture.spotReportInfo)
                    val suggestions = listOf(SuggestionFixture.spotSuggestionInfo)

                    every { reportService.findAllReportByUserId(userId) } returns reports
                    every { suggestionService.findAllSpotSuggestionByUser(userId) } returns suggestions

                    val result = historyFacade.findHistories(userId)

                    result shouldHaveSize 2
                    result.any { it.actionType == SpotActionType.REPORT } shouldBe true
                    result.any { it.actionType == SpotActionType.SUGGESTION } shouldBe true
                    verify { reportService.findAllReportByUserId(userId) }
                    verify { suggestionService.findAllSpotSuggestionByUser(userId) }
                }
            }

            context("신고 내역만 있는 경우") {
                it("신고 내역만 반환한다") {
                    val userId = 1L
                    val reports = listOf(ReportFixture.spotReportInfo)

                    every { reportService.findAllReportByUserId(userId) } returns reports
                    every { suggestionService.findAllSpotSuggestionByUser(userId) } returns emptyList()

                    val result = historyFacade.findHistories(userId)

                    result shouldHaveSize 1
                    result.first().actionType shouldBe SpotActionType.REPORT
                }
            }

            context("제보 내역만 있는 경우") {
                it("제보 내역만 반환한다") {
                    val userId = 1L
                    val suggestions = listOf(SuggestionFixture.spotSuggestionInfo)

                    every { reportService.findAllReportByUserId(userId) } returns emptyList()
                    every { suggestionService.findAllSpotSuggestionByUser(userId) } returns suggestions

                    val result = historyFacade.findHistories(userId)

                    result shouldHaveSize 1
                    result.first().actionType shouldBe SpotActionType.SUGGESTION
                }
            }

            context("내역이 없는 경우") {
                it("빈 목록을 반환한다") {
                    val userId = 999L

                    every { reportService.findAllReportByUserId(userId) } returns emptyList()
                    every { suggestionService.findAllSpotSuggestionByUser(userId) } returns emptyList()

                    val result = historyFacade.findHistories(userId)

                    result.shouldBeEmpty()
                }
            }

            context("상태 변환이 올바른 경우") {
                it("신고 상태가 내역 상태로 변환된다") {
                    val userId = 1L
                    val reports =
                        listOf(
                            ReportFixture.spotReportInfo.copy(status = ReportStatus.APPROVE),
                            ReportFixture.spotReportInfo.copy(id = 2L, status = ReportStatus.REJECT),
                            ReportFixture.spotReportInfo.copy(id = 3L, status = ReportStatus.WAITING),
                        )

                    every { reportService.findAllReportByUserId(userId) } returns reports
                    every { suggestionService.findAllSpotSuggestionByUser(userId) } returns emptyList()

                    val result = historyFacade.findHistories(userId)

                    result shouldHaveSize 3
                    result.count { it.status == HistoryStatus.APPROVE } shouldBe 1
                    result.count { it.status == HistoryStatus.REJECT } shouldBe 1
                    result.count { it.status == HistoryStatus.WAITING } shouldBe 1
                }

                it("제보 상태가 내역 상태로 변환된다") {
                    val userId = 1L
                    val suggestions =
                        listOf(
                            SuggestionFixture.spotSuggestionInfo.copy(status = SuggestionStatus.APPROVE),
                            SuggestionFixture.spotSuggestionInfo.copy(id = 2L, status = SuggestionStatus.REJECT),
                            SuggestionFixture.spotSuggestionInfo.copy(id = 3L, status = SuggestionStatus.WAITING),
                            SuggestionFixture.spotSuggestionInfo.copy(id = 4L, status = SuggestionStatus.CANCEL),
                        )

                    every { reportService.findAllReportByUserId(userId) } returns emptyList()
                    every { suggestionService.findAllSpotSuggestionByUser(userId) } returns suggestions

                    val result = historyFacade.findHistories(userId)

                    result shouldHaveSize 4
                    result.count { it.status == HistoryStatus.APPROVE } shouldBe 1
                    result.count { it.status == HistoryStatus.REJECT } shouldBe 1
                    result.count { it.status == HistoryStatus.WAITING } shouldBe 1
                    result.count { it.status == HistoryStatus.CANCEL } shouldBe 1
                }
            }
        }
    })
