package com.sseudam.report

import com.sseudam.trashspot.TrashSpotService
import org.springframework.stereotype.Service

@Service
class ReportFacade(
    private val reportService: ReportService,
    private val trashSpotService: TrashSpotService,
) {
    fun validateSpotReport(name: String): Boolean {
        reportService.validateSpotReportName(name)
        trashSpotService.validateSpotName(name)

        return true
    }
}
