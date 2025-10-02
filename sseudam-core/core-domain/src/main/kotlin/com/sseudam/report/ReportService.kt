package com.sseudam.report

import com.sseudam.pet.PetPointAction
import com.sseudam.pet.event.UserPetContextEvent
import com.sseudam.report.event.ReportUpdateEvent
import com.sseudam.report.reject.ReportReject
import com.sseudam.support.cursor.OffsetPageRequest
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import com.sseudam.support.page.Page
import com.sseudam.support.tx.Tx
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class ReportService(
    private val reportAppender: ReportAppender,
    private val reportReader: ReportReader,
    private val reportUpdater: ReportUpdater,
    private val reportDeleter: ReportDeleter,
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

    fun findSpotReportById(reportId: Long): SpotReport.Info = reportReader.readById(reportId)

    fun findRejectReportByReportId(reportId: Long): ReportReject.Info? = reportReader.readRejectByReportId(reportId)

    fun updateSpotReport(updateReport: UpdateReport): SpotReport.Info =
        Tx.writeable {
            val report = reportUpdater.update(updateReport.reportId, updateReport.status)
            applicationEventPublisher.publishEvent(
                ReportUpdateEvent(
                    report,
                ),
            )
            when (report.status) {
                ReportStatus.APPROVE -> {
                    reportDeleter.deleteBy(updateReport.reportId)
                    applicationEventPublisher.publishEvent(
                        UserPetContextEvent(
                            userId = report.userId,
                            petPointAction = PetPointAction.REPORT_APPROVED,
                        ),
                    )
                }
                ReportStatus.REJECT -> {
                    if (!updateReport.reason.isNullOrBlank()) {
                        reportAppender.appendReject(report.id, updateReport.reason)
                    }
                }
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
