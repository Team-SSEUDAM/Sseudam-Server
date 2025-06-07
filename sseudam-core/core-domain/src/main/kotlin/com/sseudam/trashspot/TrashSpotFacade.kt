package com.sseudam.trashspot

import com.sseudam.suggestion.SuggestionService
import com.sseudam.support.geo.Region
import com.sseudam.trashspot.image.TrashSpotImageService
import com.sseudam.user.UserService
import org.springframework.stereotype.Service

@Service
class TrashSpotFacade(
    private val trashSpotService: TrashSpotService,
    private val trashSpotImageService: TrashSpotImageService,
    private val suggestionService: SuggestionService,
    private val userService: UserService,
) {
    fun findAll(
        region: Region?,
        location: TrashSpotLocation,
    ): List<TrashSpot> {
        val trashSpots = trashSpotService.findAll(region, location)
        return trashSpots
    }

    fun findDetails(spotId: Long): TrashSpotDetail {
        val spot = trashSpotService.findOne(spotId)
        val image = trashSpotImageService.findBySpotId(spotId).lastOrNull()
        val suggestioner = suggestionService.findSpotSuggestionBySite(spot.address.site)
        val user = suggestioner?.let { userService.getProfile(it.userId) }
        return TrashSpotDetail(spot, image, user)
    }
}
