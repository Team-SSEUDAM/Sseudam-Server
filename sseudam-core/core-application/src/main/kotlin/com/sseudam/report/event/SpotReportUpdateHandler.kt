package com.sseudam.report.event

import com.sseudam.pet.PetPointAction
import com.sseudam.pet.event.UserPetContextEvent
import com.sseudam.report.component.ReportAppender
import com.sseudam.report.component.ReportDeleter
import com.sseudam.support.CacheRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Component

@Component
class SpotReportUpdateHandler(
    private val reportDeleter: ReportDeleter,
    private val reportAppender: ReportAppender,
    private val cacheRepository: CacheRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    companion object {
        private const val SPOT_DETAIL_CACHE_KEY_PREFIX = "spot:detail:"
    }

    @ApplicationModuleListener(
        id = "spot-report-update-approve",
        condition = "#event.report.status.name() == 'APPROVE'",
    )
    fun handleApprove(event: SpotReportUpdateEvent) {
        reportDeleter.deleteBy(event.report.id)
        applicationEventPublisher.publishEvent(
            UserPetContextEvent(
                userId = event.report.userId,
                petPointAction = PetPointAction.REPORT_APPROVED,
            ),
        )
        cacheRepository.delete(SPOT_DETAIL_CACHE_KEY_PREFIX + event.report.spotId)
    }

    @ApplicationModuleListener(
        id = "spot-report-update-reject-reason",
        condition = "#event.report.status.name() == 'REJECT' && #event.reason != null && #event.reason.trim().length() > 0",
    )
    fun handleReject(event: SpotReportUpdateEvent) {
        val reason = event.reason ?: return
        reportAppender.appendReject(event.report.id, event.reason)
    }
}
