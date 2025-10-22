package com.sseudam.storage.db.core.suggestion

import com.sseudam.storage.db.core.suggestion.model.SpotSuggestionEntityWithReject
import com.sseudam.storage.db.core.suggestion.reject.SuggestionRejectEntity
import com.sseudam.storage.db.core.support.JDSLExtensions
import com.sseudam.suggestion.SpotSuggestion
import com.sseudam.suggestion.SuggestionStatus
import com.sseudam.support.cursor.OffsetPageRequest
import com.sseudam.support.page.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository

@Repository
class SpotSuggestionCustomRepository(
    private val spotSuggestionJpaRepository: SpotSuggestionJpaRepository,
) {
    fun findAllBy(
        offsetPageRequest: OffsetPageRequest,
        searchStatus: SuggestionStatus?,
    ): Page<SpotSuggestion.Detail> {
        val pageable =
            PageRequest.of(
                offsetPageRequest.page,
                offsetPageRequest.size,
                Sort.by(Sort.Direction.DESC, "createdAt"),
            )

        val suggestions =
            spotSuggestionJpaRepository.findPage(JDSLExtensions, pageable) {
                selectNew<SpotSuggestionEntityWithReject>(
                    entity(SpotSuggestionEntity::class),
                    entity(SuggestionRejectEntity::class).path(SuggestionRejectEntity::reason),
                ).from(
                    entity(SpotSuggestionEntity::class),
                    leftJoin(SuggestionRejectEntity::class).on(
                        path(SpotSuggestionEntity::id)
                            .eq(path(SuggestionRejectEntity::suggestionId)),
                    ),
                ).whereAnd(
                    searchStatus?.let {
                        path(SpotSuggestionEntity::status).eq(it)
                    },
                ).orderBy(
                    path(SpotSuggestionEntity::createdAt).desc(),
                )
            }

        return Page.of(
            content =
                suggestions.content.mapNotNull { result ->
                    result?.let {
                        SpotSuggestion.Detail
                            .of(it.entity.toSpotSuggestion(), null)
                            .copy(rejectReason = it.rejectReason)
                    }
                },
            totalCount = suggestions.totalElements,
        )
    }
}
