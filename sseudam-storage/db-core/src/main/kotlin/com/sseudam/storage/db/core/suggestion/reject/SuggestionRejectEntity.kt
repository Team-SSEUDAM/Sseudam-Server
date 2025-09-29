package com.sseudam.storage.db.core.suggestion.reject

import com.sseudam.storage.db.core.support.BaseEntity
import com.sseudam.suggestion.reject.SuggestionReject
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "t_reject_suggestion")
class SuggestionRejectEntity(
    val suggestionId: Long,
    val reason: String?,
) : BaseEntity() {
    constructor(
        create: SuggestionReject.Create,
    ) : this (
        suggestionId = create.suggestionId,
        reason = create.reason,
    )

    fun toSuggestionReject(): SuggestionReject.Info =
        SuggestionReject.Info(
            suggestionId = suggestionId,
            reason = reason,
            createdAt = createdAt,
        )
}
