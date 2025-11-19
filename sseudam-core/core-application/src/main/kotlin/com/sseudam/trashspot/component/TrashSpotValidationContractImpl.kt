package com.sseudam.trashspot.component

import com.sseudam.contract.trashspot.TrashSpotValidationContract
import org.locationtech.jts.geom.Point
import org.springframework.stereotype.Component

/**
 * TrashSpot 제보 검증 구현체
 *
 * Suggestion 모듈에서 필요한 TrashSpot 검증 기능을 제공합니다.
 */
@Component
class TrashSpotValidationContractImpl(
    private val trashSpotReader: TrashSpotReader,
    private val trashSpotValidator: TrashSpotValidator,
) : TrashSpotValidationContract {
    override fun existsByName(name: String): Boolean = trashSpotReader.existsByName(name)

    override fun verifyPoint(point: Point) {
        trashSpotValidator.verifyPoint(point)
    }
}
