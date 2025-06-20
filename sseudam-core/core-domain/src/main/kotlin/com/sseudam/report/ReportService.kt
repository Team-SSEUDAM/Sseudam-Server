package com.sseudam.report

import com.sseudam.common.ImageS3Caller
import com.sseudam.common.S3ImageUrl
import com.sseudam.pet.PetPointAction
import com.sseudam.pet.event.PetEventPublisher
import com.sseudam.report.event.ReportEventPublisher
import com.sseudam.support.cursor.OffsetPageRequest
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ReportService(
    private val reportAppender: ReportAppender,
    private val reportReader: ReportReader,
    private val reportUpdater: ReportUpdater,
    private val reportEventPublisher: ReportEventPublisher,
    private val petEventPublisher: PetEventPublisher,
    private val imageS3Caller: ImageS3Caller,
) {
    companion object {
        private const val REPORT_IMAGE_PATH = "report"
    }

    fun createSpotReport(report: SpotReport.Create): Pair<SpotReport.Info, S3ImageUrl> {
        val uploadUrl = imageS3Caller.createUploadUrl(report.userId, LocalDateTime.now(), REPORT_IMAGE_PATH)
        val spotReport = reportAppender.append(uploadUrl.imageUrl, report)

        petEventPublisher.publish(report.userId, PetPointAction.REPORT)
        return spotReport to uploadUrl
    }

    fun findAllReportByUserId(userId: Long): List<SpotReport.Info> = reportReader.readAllByUserId(userId)

    fun findReportsBy(
        offsetPageRequest: OffsetPageRequest,
        searchType: ReportType?,
    ): List<SpotReport.Info> = reportReader.readAllBy(offsetPageRequest, searchType)

    fun findSpotReportById(reportId: Long): SpotReport.Info = reportReader.readById(reportId)

    fun updateSpotReport(updateReport: UpdateReport): SpotReport.Info {
        val report = reportUpdater.update(updateReport.reportId, updateReport.status)
        if (report.status == ReportStatus.APPROVE) {
            reportEventPublisher.publish(report)
            petEventPublisher.publish(report.userId, PetPointAction.REPORT_APPROVED)
        }
        return report
    }
}
