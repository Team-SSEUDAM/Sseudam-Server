package com.sseudam.history

import com.sseudam.history.dto.HistoryStatus
import com.sseudam.history.dto.SpotActionType
import com.sseudam.history.dto.SpotHistory
import com.sseudam.report.ReportService
import com.sseudam.report.ReportStatus
import com.sseudam.report.SpotReport
import com.sseudam.suggestion.SpotSuggestion
import com.sseudam.suggestion.SuggestionService
import com.sseudam.suggestion.SuggestionStatus
import com.sseudam.support.Cache
import com.sseudam.support.extension.parZipWithMDC
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import tools.jackson.core.type.TypeReference

@Service
class HistoryFacade(
    private val reportService: ReportService,
    private val suggestionService: SuggestionService,
) {
    fun findHistories(userId: Long): List<SpotHistory.Info> =
        Cache.cache(
            key = "user:$userId:histories",
            ttl = 10,
            typeReference = object : TypeReference<List<SpotHistory.Info>>() {},
        ) {
            runBlocking {
                parZipWithMDC(
                    { reportService.findAllReportByUserId(userId).map { it.toSpotHistoryInfo() } },
                    { suggestionService.findAllSpotSuggestionByUser(userId).map { it.toSpotHistoryInfo() } },
                ) { reports, suggestions ->
                    (reports + suggestions).sortedByDescending { it.createdAt }
                }
            }
        }

    private fun SpotReport.Info.toSpotHistoryInfo() =
        SpotHistory.Info(
            id = this.id,
            spotId = this.spotId,
            userId = this.userId,
            point = this.point,
            spotName = this.spotName,
            address = this.address,
            trashType = this.trashType,
            imageUrl = this.imageUrl,
            actionType = SpotActionType.REPORT,
            status =
                when (this.status) {
                    ReportStatus.APPROVE -> HistoryStatus.APPROVE
                    ReportStatus.REJECT -> HistoryStatus.REJECT
                    ReportStatus.WAITING -> HistoryStatus.WAITING
                    ReportStatus.CANCEL -> HistoryStatus.CANCEL
                },
            createdAt = this.createdAt,
        )

    private fun SpotSuggestion.Info.toSpotHistoryInfo() =
        SpotHistory.Info(
            id = this.id,
            spotId = null,
            userId = this.userId,
            point = this.point,
            spotName = this.spotName,
            address = this.address,
            trashType = this.trashType,
            imageUrl = this.imageUrl,
            actionType = SpotActionType.SUGGESTION,
            status =
                when (this.status) {
                    SuggestionStatus.APPROVE -> HistoryStatus.APPROVE
                    SuggestionStatus.REJECT -> HistoryStatus.REJECT
                    SuggestionStatus.WAITING -> HistoryStatus.WAITING
                    SuggestionStatus.CANCEL -> HistoryStatus.CANCEL
                },
            createdAt = this.createdAt,
        )
}
