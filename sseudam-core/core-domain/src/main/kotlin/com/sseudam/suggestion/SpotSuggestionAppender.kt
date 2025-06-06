package com.sseudam.suggestion

import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.springframework.stereotype.Component

@Component
class SpotSuggestionAppender(
    private val spotSuggestionRepository: SpotSuggestionRepository,
) {
    companion object {
        private val GEOMETRY_FACTORY = GeometryFactory()
    }

    fun append(
        imageUrl: String,
        createSpotSuggestion: SpotSuggestion.Create,
    ): SpotSuggestion.Info {
        val point = GEOMETRY_FACTORY.createPoint(Coordinate(createSpotSuggestion.longitude, createSpotSuggestion.latitude))
        return spotSuggestionRepository.create(imageUrl, point, createSpotSuggestion)
    }
}
