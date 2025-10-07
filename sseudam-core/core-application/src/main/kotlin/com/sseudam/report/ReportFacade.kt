package com.sseudam.report

import com.sseudam.common.ImageS3Caller
import com.sseudam.common.S3ImageUrl
import com.sseudam.pet.PetPointAction
import com.sseudam.report.event.SpotReportCreatedEvent
import com.sseudam.report.result.CreateSpotReportResult
import com.sseudam.support.Cache
import com.sseudam.trashspot.TrashSpotService
import com.sseudam.trashspot.image.TrashSpotImageService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReportFacade(
    private val reportService: ReportService,
    private val trashSpotService: TrashSpotService,
    private val trashSpotImageService: TrashSpotImageService,
    private val imageS3Caller: ImageS3Caller,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    companion object {
        private const val REPORT_IMAGE_PATH = "report"
        private const val DEFAULT_REPORT_IMAGE_URL = "https://img.sseudam.me/dev/default_trash_profile.webp"
    }

    fun findReportDetails(reportId: Long): SpotReport.Detail {
        val reportInfo = reportService.findSpotReportById(reportId)
        val rejectReport = reportService.findRejectReportByReportId(reportId)
        return SpotReport.Detail.of(reportInfo, rejectReport)
    }

    fun validateSpotReport(name: String): Boolean {
        reportService.validateSpotReportName(name)
        trashSpotService.validateSpotName(name)

        return true
    }

    @Transactional
    fun createSpotReport(create: SpotReport.Create): CreateSpotReportResult {
        val presignedUrl: String?
        val images = trashSpotImageService.findBySpotId(create.spotId)
        var imageUrl =
            images
                .filter { it.updatedAt != null }
                .maxByOrNull { it.updatedAt!! }
                ?.imageUrl ?: DEFAULT_REPORT_IMAGE_URL
        if (create.reportType == ReportType.PHOTO) {
            val s3ImageUrl: S3ImageUrl = imageS3Caller.createUploadUrl(create.userId, REPORT_IMAGE_PATH + "/${create.spotId}")
            presignedUrl = s3ImageUrl.presignedUrl
            imageUrl = s3ImageUrl.imageUrl
        } else {
            presignedUrl = null
        }

        val spotReport =
            reportService.appendReport(imageUrl, create).apply {
                Cache.delete("user:${create.userId}:histories")
            }

        return CreateSpotReportResult(spotReport, presignedUrl).also {
            applicationEventPublisher.publishEvent(
                SpotReportCreatedEvent(
                    spotReport = spotReport,
                    userId = create.userId,
                    petPointAction = PetPointAction.REPORT,
                ),
            )
        }
    }
}
