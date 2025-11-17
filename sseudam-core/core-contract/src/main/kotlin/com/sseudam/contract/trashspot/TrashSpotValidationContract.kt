package com.sseudam.contract.trashspot

import org.locationtech.jts.geom.Point

/**
 * TrashSpot 제보 검증을 위한 공개 계약 인터페이스
 *
 * Suggestion 모듈에서 TrashSpot 정보를 검증할 때 사용합니다.
 * 순환 의존성을 방지하기 위해 중립적인 core-contract 모듈에 정의되어 있습니다.
 */
interface TrashSpotValidationContract {
    fun existsByName(name: String): Boolean

    fun verifyPoint(point: Point)
}
