package com.sseudam.suggestion

import org.locationtech.jts.geom.Point

interface SpotSuggestionRepository {
    fun create(
        imageUrl: String,
        point: Point,
        createSpotSuggestion: SpotSuggestion.Create,
    ): SpotSuggestion.Info

    fun findAllByUserId(userId: Long): List<SpotSuggestion.Info>

    fun findBySite(site: String): SpotSuggestion.Info?
}
