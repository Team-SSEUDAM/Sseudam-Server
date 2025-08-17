package com.sseudam.presentation.v1.history.response

import com.sseudam.history.SpotHistory

data class HistoryAllResponse(
    val list: List<HistoryResponse>,
) {
    companion object {
        fun from(histories: List<SpotHistory.Info>): HistoryAllResponse = HistoryAllResponse(histories.map { HistoryResponse.from(it) })
    }
}
