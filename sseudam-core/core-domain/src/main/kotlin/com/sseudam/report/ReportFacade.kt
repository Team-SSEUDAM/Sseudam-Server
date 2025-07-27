package com.sseudam.report

import com.sseudam.common.ImageS3Caller
import com.sseudam.common.S3ImageUrl
import com.sseudam.pet.PetPointAction
import com.sseudam.pet.event.PetEventPublisher
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import com.sseudam.support.tx.TxAdvice
import com.sseudam.trashspot.TrashSpotService
import com.sseudam.trashspot.image.TrashSpotImageService
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ReportFacade(
    private val reportService: ReportService,
    private val trashSpotService: TrashSpotService,
    private val trashSpotImageService: TrashSpotImageService,
    private val imageS3Caller: ImageS3Caller,
    private val petEventPublisher: PetEventPublisher,
    private val txAdvice: TxAdvice,
) {
    companion object {
        private const val REPORT_IMAGE_PATH = "report"
    }

    fun validateSpotReport(name: String): Boolean {
        reportService.validateSpotReportName(name)
        trashSpotService.validateSpotName(name)

        return true
    }

    fun createSpotReport(report: SpotReport.Create): Pair<SpotReport.Info, String?> =
        txAdvice.write {
            val presignedUrl: String?
            val images = trashSpotImageService.findBySpotId(report.spotId)
            var imageUrl =
                images
                    .filter { it.updatedAt != null }
                    .maxByOrNull { it.updatedAt!! }
                    ?.imageUrl
                    ?: throw ErrorException(ErrorType.NOT_FOUND_DATA)
            if (report.reportType == ReportType.PHOTO) {
                val s3ImageUrl: S3ImageUrl = imageS3Caller.createUploadUrl(report.userId, LocalDateTime.now(), REPORT_IMAGE_PATH)
                presignedUrl = s3ImageUrl.presignedUrl
                imageUrl = s3ImageUrl.imageUrl
            } else {
                presignedUrl = null
            }

            val spotReport = reportService.appendReport(imageUrl, report)
            petEventPublisher.publish(report.userId, PetPointAction.REPORT)

            return@write spotReport to presignedUrl
        }
}
