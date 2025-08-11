package com.sseudam.history

import com.sseudam.report.ReportService
import com.sseudam.report.SpotReport
import com.sseudam.suggestion.SpotSuggestion
import com.sseudam.suggestion.SuggestionService
import org.springframework.stereotype.Service

@Service
class HistoryFacade(
    private val reportService: ReportService,
    private val suggestionService: SuggestionService,
) {
    fun findHistories(
        userId: Long,
        action: SpotActionType?,
    ): List<SpotHistory.Info> =
        when (action) {
            SpotActionType.REPORT -> {
                reportService
                    .findAllReportByUserId(userId)
                    .map { it.toSpotHistoryInfo() }
            }
            SpotActionType.SUGGESTION -> {
                suggestionService
                    .findAllSpotSuggestionByUser(userId)
                    .map { it.toSpotHistoryInfo() }
            }
            else -> {
                val reports =
                    reportService
                        .findAllReportByUserId(userId)
                        .map { it.toSpotHistoryInfo() }
                val suggestions =
                    suggestionService
                        .findAllSpotSuggestionByUser(userId)
                        .map { it.toSpotHistoryInfo() }
                (reports + suggestions).sortedByDescending { it.createdAt }
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
            createdAt = this.createdAt,
        )
}
