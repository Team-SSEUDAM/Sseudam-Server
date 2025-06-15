package com.sseudam.trashspot

import com.sseudam.common.GeoConverter
import com.sseudam.report.ReportType
import com.sseudam.report.SpotReport
import com.sseudam.suggestion.SpotSuggestion
import com.sseudam.support.geo.GeoJson
import com.sseudam.support.geo.Region
import org.springframework.stereotype.Service

@Service
class TrashSpotService(
    private val trashSpotReader: TrashSpotReader,
    private val trashSpotAppender: TrashSpotAppender,
    private val trashSpotUpdater: TrashSpotUpdater,
    private val geoConverter: GeoConverter,
) {
    fun createTrashSpotBySuggestion(suggestionInfo: SpotSuggestion.Info): TrashSpot.Info =
        trashSpotAppender.append(
            TrashSpot.Create(
                name = suggestionInfo.spotName,
                region = suggestionInfo.region,
                trashType = suggestionInfo.trashType,
                address = suggestionInfo.address,
                point = geoConverter.geoJsonPointToJtsPoint(suggestionInfo.point as GeoJson.Point),
            ),
        )

    fun findAll(
        region: Region?,
        trashType: TrashType?,
        location: TrashSpotLocation,
    ): List<TrashSpot.Info> {
        val condition =
            when {
                location.isNotSet() && trashType != null -> FindTrashSpotPolicyCondition.ByTypeAndLocation(trashType, location)
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

    fun updateByReport(report: SpotReport.Info) {
        when (report.reportType) {
            ReportType.KIND -> {
                trashSpotUpdater.updateType(report.spotId, report.trashType)
            }
            ReportType.NAME -> {
                trashSpotUpdater.updateName(report.spotId, report.spotName)
            }
            ReportType.POINT -> {
                val jtsPoint = geoConverter.geoJsonPointToJtsPoint(report.point as GeoJson.Point)
                trashSpotUpdater.updateLocation(report.spotId, jtsPoint)
            }
            else -> {}
        }
    }
}
