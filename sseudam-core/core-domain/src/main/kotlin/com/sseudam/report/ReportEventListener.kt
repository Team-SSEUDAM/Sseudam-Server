package com.sseudam.report

import com.sseudam.report.event.ReportUpdateEvent
import com.sseudam.trashspot.TrashSpotService
import com.sseudam.trashspot.image.TrashSpotImageService
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class ReportEventListener(
    private val trashSpotService: TrashSpotService,
    private val trashSpotImageService: TrashSpotImageService,
) {
    @Async
    @EventListener
    fun updateReportListener(event: ReportUpdateEvent) {
        when (event.report.reportType) {
            ReportType.PHOTO -> {
                trashSpotImageService.updateImage(
                    event.report.spotId,
                    event.report.imageUrl,
                )
            }
            else -> {
                trashSpotService.updateByReport(event.report)
            }
        }
    }
}
