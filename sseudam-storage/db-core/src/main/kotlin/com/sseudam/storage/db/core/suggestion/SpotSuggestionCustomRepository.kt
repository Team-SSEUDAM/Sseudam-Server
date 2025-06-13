package com.sseudam.storage.db.core.suggestion

import com.sseudam.suggestion.SpotSuggestion
import com.sseudam.support.cursor.OffsetPageRequest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository

@Repository
class SpotSuggestionCustomRepository(
    private val spotSuggestionJpaRepository: SpotSuggestionJpaRepository,
) {
    fun findAllBy(offsetPageRequest: OffsetPageRequest): List<SpotSuggestion.Info> {
        val pageable =
            PageRequest.of(
                offsetPageRequest.page,
                offsetPageRequest.size,
                Sort.by(Sort.Direction.DESC, "createdAt"),
            )

        val suggestions =
            spotSuggestionJpaRepository.findPage(pageable) {
                select(entity(SpotSuggestionEntity::class))
                    .from(entity(SpotSuggestionEntity::class))
                    .whereAnd(
                        path(SpotSuggestionEntity::deletedAt).isNull(),
                    )
            }
        return suggestions.map { it!!.toSpotSuggestion() }.toList()
    }
}
