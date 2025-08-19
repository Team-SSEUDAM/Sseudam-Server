package com.sseudam.report

import com.sseudam.pet.PetPointAction
import com.sseudam.pet.event.PetEventPublisher
import com.sseudam.report.event.ReportEventPublisher
import com.sseudam.report.reject.ReportReject
import com.sseudam.support.cursor.OffsetPageRequest
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import com.sseudam.support.page.Page
import com.sseudam.support.tx.TxAdvice
import org.springframework.stereotype.Service

@Service
class ReportService(
    private val reportAppender: ReportAppender,
    private val reportReader: ReportReader,
    private val reportUpdater: ReportUpdater,
    private val reportDeleter: ReportDeleter,
    private val txAdvice: TxAdvice,
    private val reportEventPublisher: ReportEventPublisher,
    private val petEventPublisher: PetEventPublisher,
) {
    fun appendReport(
        imageUrl: String,
        report: SpotReport.Create,
    ): SpotReport.Info = reportAppender.append(imageUrl, report)

    fun findAllReportByUserId(userId: Long): List<SpotReport.Info> = reportReader.readAllByUserId(userId)

    fun findReportsBy(
        offsetPageRequest: OffsetPageRequest,
        searchType: ReportType?,
    ): Page<SpotReport.Info> = reportReader.readAllBy(offsetPageRequest, searchType)

    fun findSpotReportById(reportId: Long): SpotReport.Info = reportReader.readById(reportId)

    fun findRejectReportByReportId(reportId: Long): ReportReject.Info? = reportReader.readRejectByReportId(reportId)

    fun updateSpotReport(updateReport: UpdateReport): SpotReport.Info =
        txAdvice.write {
            val report = reportUpdater.update(updateReport.reportId, updateReport.status)
            reportEventPublisher.publish(report)

            when (report.status) {
                ReportStatus.APPROVE -> {
                    reportDeleter.deleteBy(updateReport.reportId)
                    petEventPublisher.publish(report.userId, PetPointAction.REPORT_APPROVED)
                }
                ReportStatus.REJECT -> reportAppender.appendReject(report.id, updateReport.reason)
                else -> {}
            }

            report
        }

    fun validateSpotReportName(name: String) {
        if (reportReader.existsByName(name)) {
            throw ErrorException(ErrorType.DUPLICATE_SPOT_NAME)
        }
    }
}
