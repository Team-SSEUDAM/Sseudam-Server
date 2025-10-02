package com.sseudam.report.event

import com.sseudam.pet.PetPointAction
import com.sseudam.pet.event.UserPetContextEvent
import com.sseudam.report.ReportAppender
import com.sseudam.report.ReportDeleter
import com.sseudam.report.ReportStatus
import org.springframework.context.ApplicationEventPublisher
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Component

@Component
class SpotReportUpdateHandler(
    private val reportDeleter: ReportDeleter,
    private val reportAppender: ReportAppender,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    @ApplicationModuleListener(id = "spot-report-update-approve")
    fun handleApprove(event: SpotReportUpdateEvent) {
        if (event.report.status == ReportStatus.APPROVE) {
            reportDeleter.deleteBy(event.report.id)
            applicationEventPublisher.publishEvent(
                UserPetContextEvent(
                    userId = event.report.userId,
                    petPointAction = PetPointAction.REPORT_APPROVED,
                ),
            )
        }
    }

    @ApplicationModuleListener(id = "spot-report-update-reject")
    fun handleReject(event: SpotReportUpdateEvent) {
        if (event.report.status == ReportStatus.REJECT && !event.reason.isNullOrBlank()) {
            reportAppender.appendReject(event.report.id, event.reason)
        }
    }
}
