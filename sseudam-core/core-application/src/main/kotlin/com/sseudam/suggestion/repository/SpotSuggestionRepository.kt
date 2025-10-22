package com.sseudam.suggestion.repository

import com.sseudam.suggestion.SpotSuggestion
import com.sseudam.suggestion.SuggestionStatus
import com.sseudam.support.cursor.OffsetPageRequest
import com.sseudam.support.page.Page
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

    fun findByPoint(point: Point): SpotSuggestion.Info?

    fun findAllBy(
        offsetPageRequest: OffsetPageRequest,
        searchStatus: SuggestionStatus?,
    ): Page<SpotSuggestion.Detail>

    fun update(
        suggestionId: Long,
        status: SuggestionStatus,
    ): SpotSuggestion.Info

    fun existsByName(name: String): Boolean

    fun deleteBy(suggestionId: Long)
}
