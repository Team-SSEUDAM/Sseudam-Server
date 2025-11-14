package com.sseudam.trashspot

import com.fasterxml.jackson.core.type.TypeReference
import com.sseudam.common.GeoConverter
import com.sseudam.common.Region
import com.sseudam.common.TrashType
import com.sseudam.support.Cache
import com.sseudam.trashspot.dto.TrashSpotLocation
import com.sseudam.trashspot.image.TrashSpotImageService
import com.sseudam.trashspot.result.TrashSpotDetail
import com.sseudam.user.UserService
import com.sseudam.visit.SpotVisitedService
import org.springframework.stereotype.Service

@Service
class TrashSpotFacade(
    private val trashSpotService: TrashSpotService,
    private val trashSpotImageService: TrashSpotImageService,
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
            val visitedCount = visitedService.countBySpotId(spotId)
            return@cache TrashSpotDetail(spot, image, null, visitedCount)
        }
}
