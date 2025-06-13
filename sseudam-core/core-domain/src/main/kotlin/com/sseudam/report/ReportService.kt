package com.sseudam.report

import com.sseudam.common.ImageS3Caller
import com.sseudam.common.S3ImageUrl
import com.sseudam.support.cursor.OffsetPageRequest
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ReportService(
    private val reportAppender: ReportAppender,
    private val reportReader: ReportReader,
    private val imageS3Caller: ImageS3Caller,
) {
    companion object {
        const val REPORT_IMAGE_PATH = "report"
    }

    fun createSpotReport(report: SpotReport.Create): Pair<SpotReport.Info, S3ImageUrl> {
        val createUploadUrl =
            imageS3Caller.createUploadUrl(
                report.userId,
                LocalDateTime.now(),
                REPORT_IMAGE_PATH,
            )
        val spotReport = reportAppender.append(createUploadUrl.imageUrl, report)
        return spotReport to createUploadUrl
    }

    fun findAllReportByUserId(userId: Long): List<SpotReport.Info> = reportReader.readAllByUserId(userId)

    fun findReportsBy(
        offsetPageRequest: OffsetPageRequest,
        searchType: ReportType?,
    ): List<SpotReport.Info> = reportReader.readAllBy(offsetPageRequest, searchType)

    fun findSpotReportById(reportId: Long): SpotReport.Info = reportReader.readById(reportId)
}
