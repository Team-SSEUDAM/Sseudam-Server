package com.sseudam.suggestion.component

import com.sseudam.suggestion.SpotSuggestion
import com.sseudam.suggestion.reject.SuggestionReject
import com.sseudam.suggestion.repository.SpotSuggestionRepository
import com.sseudam.suggestion.repository.SuggestionRejectRepository
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.PrecisionModel
import org.springframework.stereotype.Component

@Component
class SuggestionAppender(
    private val spotSuggestionRepository: SpotSuggestionRepository,
    private val suggestionRejectRepository: SuggestionRejectRepository,
) {
    companion object {
        private val GEOMETRY_FACTORY = GeometryFactory(PrecisionModel(), 4326)
    }

    fun append(
        imageUrl: String,
        createSpotSuggestion: SpotSuggestion.Create,
    ): SpotSuggestion.Info {
        val point =
            GEOMETRY_FACTORY.createPoint(
                Coordinate(createSpotSuggestion.longitude, createSpotSuggestion.latitude),
            )
        return spotSuggestionRepository.create(imageUrl, point, createSpotSuggestion)
    }

    fun appendReject(
        suggestionId: Long,
        reason: String?,
    ) {
        suggestionRejectRepository.save(SuggestionReject.Create(suggestionId, reason ?: ""))
    }
}
