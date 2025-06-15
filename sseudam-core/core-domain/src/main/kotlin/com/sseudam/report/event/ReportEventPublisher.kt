package com.sseudam.report.event

import com.sseudam.report.SpotReport
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class ReportEventPublisher(
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    fun publish(report: SpotReport.Info) {
        applicationEventPublisher.publishEvent(
            ReportUpdateEvent(
                report,
            ),
        )
    }
}
