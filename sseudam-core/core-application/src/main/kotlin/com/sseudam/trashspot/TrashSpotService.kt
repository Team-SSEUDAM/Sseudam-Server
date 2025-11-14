package com.sseudam.trashspot

import com.sseudam.common.GeoConverter
import com.sseudam.common.GeoJson
import com.sseudam.common.Region
import com.sseudam.common.TrashType
import com.sseudam.suggestion.SpotSuggestion
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import com.sseudam.trashspot.component.FindTrashSpotPolicyCondition
import com.sseudam.trashspot.component.TrashSpotAppender
import com.sseudam.trashspot.component.TrashSpotReader
import com.sseudam.trashspot.component.TrashSpotValidator
import com.sseudam.trashspot.dto.TrashSpotLocation
import com.sseudam.trashspot.dto.isNotSet
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.PrecisionModel
import org.springframework.stereotype.Service

@Service
class TrashSpotService(
    private val trashSpotReader: TrashSpotReader,
    private val trashSpotAppender: TrashSpotAppender,
    private val trashSpotValidator: TrashSpotValidator,
    private val geoConverter: GeoConverter,
) {
    companion object {
        private val GEOMETRY_FACTORY = GeometryFactory(PrecisionModel(), 4326)
    }

    fun createTrashSpotBySuggestion(suggestionInfo: SpotSuggestion.Info): TrashSpot.Info =
        trashSpotAppender.append(
            TrashSpot.Create(
                name = suggestionInfo.spotName,
                region = suggestionInfo.region,
                trashType = suggestionInfo.trashType,
                address = suggestionInfo.address,
                point = geoConverter.geoJsonPointToJtsPoint(suggestionInfo.point as GeoJson.Point),
                suggesterId = suggestionInfo.userId,
            ),
        )

    fun findAll(
        region: Region?,
        trashType: TrashType?,
        location: TrashSpotLocation,
    ): List<TrashSpot.Info> {
        val condition =
            when {
                location.isNotSet() && trashType != null ->
                    FindTrashSpotPolicyCondition.ByTypeAndLocation(
                        trashType,
                        location,
                    )
                !location.isNotSet() && trashType != null -> FindTrashSpotPolicyCondition.ByType(trashType)
                !location.isNotSet() && region == null -> FindTrashSpotPolicyCondition.All
                !location.isNotSet() -> FindTrashSpotPolicyCondition.ByRegion(region!!)
                region == null -> FindTrashSpotPolicyCondition.ByLocation(location)
                else -> FindTrashSpotPolicyCondition.ByRegionAndLocation(region, location)
            }
        return trashSpotReader.findByCondition(condition)
    }

    fun findBy(spotId: Long): TrashSpot.Info = trashSpotReader.readBy(spotId)

    fun findAllByIds(spotIds: List<Long>): List<TrashSpot.Info> = trashSpotReader.readAllByIds(spotIds)

    fun validateSpotName(name: String) {
        if (trashSpotReader.existsByName(name)) {
            throw ErrorException(ErrorType.DUPLICATE_SPOT_NAME)
        }
    }
}
