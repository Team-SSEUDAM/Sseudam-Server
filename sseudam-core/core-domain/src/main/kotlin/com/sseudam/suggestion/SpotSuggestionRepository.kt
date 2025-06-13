package com.sseudam.suggestion

import com.sseudam.support.cursor.OffsetPageRequest
import org.locationtech.jts.geom.Point

interface SpotSuggestionRepository {
    fun create(
        imageUrl: String,
        point: Point,
        createSpotSuggestion: SpotSuggestion.Create,
    ): SpotSuggestion.Info

    fun findBy(suggestionId: Long): SpotSuggestion.Info

    fun findAllByUserId(userId: Long): List<SpotSuggestion.Info>

    fun findBySite(site: String): SpotSuggestion.Info?

    fun findAllBy(offsetPageRequest: OffsetPageRequest): List<SpotSuggestion.Info>
}
