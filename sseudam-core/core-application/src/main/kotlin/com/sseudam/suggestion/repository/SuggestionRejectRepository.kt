package com.sseudam.suggestion.repository

import com.sseudam.suggestion.reject.SuggestionReject

interface SuggestionRejectRepository {
    fun save(create: SuggestionReject.Create): SuggestionReject.Info

    fun findBySuggestionId(suggestionId: Long): SuggestionReject.Info?
}
