package com.sseudam.suggestion

import org.locationtech.jts.geom.Point

interface SpotSuggestionRepository {
    fun create(
        imageUrl: String,
        point: Point,
        createSpotSuggestion: SpotSuggestion.Create,
    ): SpotSuggestion.Info
}
