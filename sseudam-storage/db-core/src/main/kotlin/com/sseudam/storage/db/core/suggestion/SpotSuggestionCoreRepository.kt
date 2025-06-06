package com.sseudam.storage.db.core.suggestion

import com.sseudam.suggestion.SpotSuggestion
import com.sseudam.suggestion.SpotSuggestionRepository
import com.sseudam.support.tx.TxAdvice
import org.locationtech.jts.geom.Point
import org.springframework.stereotype.Repository

@Repository
class SpotSuggestionCoreRepository(
    private val spotSuggestionJpaRepository: SpotSuggestionJpaRepository,
    private val txAdvice: TxAdvice,
) : SpotSuggestionRepository {
    override fun create(
        imageUrl: String,
        point: Point,
        createSpotSuggestion: SpotSuggestion.Create,
    ): SpotSuggestion.Info =
        txAdvice.write {
            spotSuggestionJpaRepository
                .save(
                    SpotSuggestionEntity(imageUrl, point, createSpotSuggestion),
                ).toSpotSuggestion()
        }
}
