package com.sseudam.report

import com.sseudam.report.command.CancelReportCommand
import com.sseudam.report.command.UpdateReportCommand
import com.sseudam.report.component.ReportAppender
import com.sseudam.report.component.ReportReader
import com.sseudam.report.component.ReportUpdater
import com.sseudam.report.component.ReportValidator
import com.sseudam.report.event.SpotReportUpdateEvent
import com.sseudam.report.reject.ReportReject
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import com.sseudam.support.page.OffsetPageRequest
import com.sseudam.support.page.Page
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReportService(
    private val reportAppender: ReportAppender,
    private val reportReader: ReportReader,
    private val reportUpdater: ReportUpdater,
    private val reportValidator: ReportValidator,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    fun appendReport(
        imageUrl: String,
        report: SpotReport.Create,
    ): SpotReport.Info = reportAppender.append(imageUrl, report)

    fun findAllReportByUserId(userId: Long): List<SpotReport.Info> = reportReader.readAllByUserId(userId)

    fun findAllDetailsByUserId(userId: Long): List<SpotReport.Detail> = reportReader.readAllDetailByUserId(userId)

    fun findReportsBy(
        offsetPageRequest: OffsetPageRequest,
        searchType: ReportType?,
    ): Page<SpotReport.Detail> = reportReader.readAllBy(offsetPageRequest, searchType)

    fun findSpotReportById(reportId: Long): SpotReport.Info = reportReader.readBy(reportId)

    fun findRejectReportByReportId(reportId: Long): ReportReject.Info? = reportReader.readRejectByReportId(reportId)

    @Transactional
    fun updateSpotReport(updateReportCommand: UpdateReportCommand): SpotReport.Info =
        reportUpdater.update(updateReportCommand.reportId, updateReportCommand.status).also { report ->
            applicationEventPublisher.publishEvent(
                SpotReportUpdateEvent(report, updateReportCommand.rejectReason),
            )
        }

    fun validateSpotReportName(name: String) {
        if (reportReader.existsByName(name)) {
            throw ErrorException(ErrorType.DUPLICATE_SPOT_NAME)
        }
    }

    fun cancel(command: CancelReportCommand) {
        val report = reportReader.readBy(command.reportId)
        reportValidator.verifyReport(command.userId, report)
        reportUpdater.cancel(command.reportId)
    }
}
