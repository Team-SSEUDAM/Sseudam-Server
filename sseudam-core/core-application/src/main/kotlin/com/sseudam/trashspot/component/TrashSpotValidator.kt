package com.sseudam.trashspot.component

import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import com.sseudam.trashspot.repository.TrashSpotRepository
import org.locationtech.jts.geom.Point
import org.springframework.stereotype.Component

@Component
class TrashSpotValidator(
    private val trashSpotRepository: TrashSpotRepository,
) {
    fun verifySite(site: String) {
        val trashSpot = trashSpotRepository.findBySite(site)
        if (trashSpot != null) {
            throw ErrorException(ErrorType.ALREADY_EXIST_SPOT_SITE)
        }
    }

    fun verifyPoint(point: Point) {
        val trashSpot = trashSpotRepository.findByPoint(point)
        if (trashSpot != null) {
            throw ErrorException(ErrorType.ALREADY_EXIST_SPOT_POINT)
        }
    }
}
