package com.sseudam.suggestion

import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.springframework.stereotype.Component

@Component
class SpotSuggestionAppender(
    private val spotSuggestionRepository: SpotSuggestionRepository,
) {
    fun append(
        imageUrl: String,
        createSpotSuggestion: SpotSuggestion.Create,
    ): SpotSuggestion.Info {
        val geometryFactory = GeometryFactory()
        val point = geometryFactory.createPoint(Coordinate(createSpotSuggestion.latitude, createSpotSuggestion.longitude))
        return spotSuggestionRepository.create(imageUrl, point, createSpotSuggestion)
    }
}
