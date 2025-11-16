package com.sseudam.contract.trashspot

import com.sseudam.trashspot.TrashSpot

/**
 * TrashSpot 조회를 위한 공개 계약 인터페이스
 *
 * 다른 모듈에서 TrashSpot 정보를 조회할 때 사용합니다.
 * 순환 의존성을 방지하기 위해 중립적인 core-contract 모듈에 정의되어 있습니다.
 */
interface TrashSpotQueryContract {
    fun findAllByIds(spotIds: List<Long>): List<TrashSpot.Info>
}
