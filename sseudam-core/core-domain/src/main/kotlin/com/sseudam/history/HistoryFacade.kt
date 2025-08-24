package com.sseudam.history

import com.fasterxml.jackson.core.type.TypeReference
import com.sseudam.report.ReportService
import com.sseudam.report.ReportStatus
import com.sseudam.report.SpotReport
import com.sseudam.suggestion.SpotSuggestion
import com.sseudam.suggestion.SuggestionService
import com.sseudam.suggestion.SuggestionStatus
import com.sseudam.support.Cache
import org.springframework.stereotype.Service

@Service
class HistoryFacade(
    private val reportService: ReportService,
    private val suggestionService: SuggestionService,
) {
    fun findHistories(userId: Long): List<SpotHistory.Info> =
        Cache.cache(
            key = "user:$userId:histories",
            ttl = 60,
            typeReference = object : TypeReference<List<SpotHistory.Info>>() {},
        ) {
            val reports =
                reportService
                    .findAllReportByUserId(userId)
                    .map { it.toSpotHistoryInfo() }
            val suggestions =
                suggestionService
                    .findAllSpotSuggestionByUser(userId)
                    .map { it.toSpotHistoryInfo() }
            return@cache (reports + suggestions).sortedByDescending { it.createdAt }
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
                },
            createdAt = this.createdAt,
        )
}
