package com.sseudam.storage.db.core.suggestion

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
    ): Page<SpotSuggestion.Info> {
        val pageable =
            PageRequest.of(
                offsetPageRequest.page,
                offsetPageRequest.size,
                Sort.by(Sort.Direction.DESC, "createdAt"),
            )

        val suggestions =
            spotSuggestionJpaRepository.findPage(JDSLExtensions, pageable) {
                select(entity(SpotSuggestionEntity::class))
                    .from(entity(SpotSuggestionEntity::class))
                    .whereAnd(
//                        path(SpotSuggestionEntity::deletedAt).isNull(),
                        searchStatus?.let {
                            path(SpotSuggestionEntity::status).eq(it)
                        },
                    )
            }

        return Page.of(
            content = suggestions.content.mapNotNull { it?.toSpotSuggestion() },
            totalCount = suggestions.totalElements,
        )
    }
}
