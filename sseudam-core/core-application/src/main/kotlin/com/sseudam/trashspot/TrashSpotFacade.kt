package com.sseudam.trashspot

import com.sseudam.common.GeoConverter
import com.sseudam.common.GeoJson
import com.sseudam.common.Region
import com.sseudam.suggestion.SuggestionService
import com.sseudam.suggestion.SuggestionStatus
import com.sseudam.support.Cache
import com.sseudam.trashspot.dto.TrashSpotLocation
import com.sseudam.trashspot.image.TrashSpotImageService
import com.sseudam.trashspot.result.TrashSpotDetail
import com.sseudam.user.UserService
import com.sseudam.visit.SpotVisitedService
import org.springframework.stereotype.Service
import tools.jackson.core.type.TypeReference

@Service
class TrashSpotFacade(
    private val trashSpotService: TrashSpotService,
    private val trashSpotImageService: TrashSpotImageService,
    private val suggestionService: SuggestionService,
    private val userService: UserService,
    private val visitedService: SpotVisitedService,
    private val geoConverter: GeoConverter,
) {
    fun findAll(
        region: Region?,
        trashType: TrashType?,
        location: TrashSpotLocation,
    ): List<TrashSpot.Info> {
        val trashSpots = trashSpotService.findAll(region, trashType, location)
        return trashSpots
    }

    fun findDetails(spotId: Long): TrashSpotDetail =
        Cache.cache(
            ttl = 10L,
            key = "spot:detail:$spotId",
            typeReference = object : TypeReference<TrashSpotDetail>() {},
        ) {
            val spot = trashSpotService.findBy(spotId)
            val image = trashSpotImageService.findBySpotId(spotId).lastOrNull()
            val suggestion =
                suggestionService.findSpotSuggestionByPointAndStatus(
                    geoConverter.geoJsonPointToJtsPoint(spot.point as GeoJson.Point),
                    SuggestionStatus.APPROVE,
                )
            val suggestioner = suggestion?.let { userService.getProfile(it.userId) }
            val visitedCount = visitedService.countBySpotId(spotId)
            return@cache TrashSpotDetail(spot, image, suggestioner, visitedCount)
        }
}
