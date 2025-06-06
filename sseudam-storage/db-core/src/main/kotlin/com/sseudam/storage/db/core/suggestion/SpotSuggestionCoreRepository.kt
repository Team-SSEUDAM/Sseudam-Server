package com.sseudam.storage.db.core.suggestion

import com.sseudam.suggestion.SpotSuggestion
import com.sseudam.suggestion.SpotSuggestionRepository
import org.locationtech.jts.geom.Point
import org.springframework.stereotype.Repository

@Repository
class SpotSuggestionCoreRepository(
    private val spotSuggestionJpaRepository: SpotSuggestionJpaRepository,
) : SpotSuggestionRepository {
    override fun create(
        imageUrl: String,
        point: Point,
        createSpotSuggestion: SpotSuggestion.Create,
    ): SpotSuggestion.Info {
        val suggestion =
            spotSuggestionJpaRepository.save(
                SpotSuggestionEntity(imageUrl, point, createSpotSuggestion),
            )
        return suggestion.toSpotSuggestion()
    }
}
