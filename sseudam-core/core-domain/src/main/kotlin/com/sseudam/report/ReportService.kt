package com.sseudam.report

import com.sseudam.common.ImageS3Caller
import com.sseudam.common.S3ImageUrl
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ReportService(
    private val spotReportAppender: SpotReportAppender,
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
        val spotReport = spotReportAppender.append(createUploadUrl.imageUrl, report)
        return spotReport to createUploadUrl
    }
}
