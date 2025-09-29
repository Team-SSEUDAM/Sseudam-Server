package com.sseudam.storage.db.core.suggestion.reject

import com.sseudam.suggestion.reject.SuggestionReject
import com.sseudam.suggestion.reject.SuggestionRejectRepository
import com.sseudam.support.tx.TxAdvice
import org.springframework.stereotype.Repository

@Repository
class SuggestionRejectCoreRepository(
    private val suggestionRejectJpaRepository: SuggestionRejectJpaRepository,
    private val txAdvice: TxAdvice,
) : SuggestionRejectRepository {
    override fun save(create: SuggestionReject.Create): SuggestionReject.Info =
        suggestionRejectJpaRepository.save(SuggestionRejectEntity(create)).toSuggestionReject()

    override fun findBySuggestionId(suggestionId: Long): SuggestionReject.Info? =
        txAdvice.readOnly {
            suggestionRejectJpaRepository.findBySuggestionId(suggestionId)?.toSuggestionReject()
        }
}
