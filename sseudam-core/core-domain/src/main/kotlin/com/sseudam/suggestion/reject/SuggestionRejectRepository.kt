package com.sseudam.suggestion.reject

interface SuggestionRejectRepository {
    fun save(create: SuggestionReject.Create): SuggestionReject.Info

    fun findBySuggestionId(suggestionId: Long): SuggestionReject.Info?
}
