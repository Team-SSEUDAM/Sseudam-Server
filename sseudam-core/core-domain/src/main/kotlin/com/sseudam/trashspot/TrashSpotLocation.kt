package com.sseudam.trashspot

data class TrashSpotLocation(
    val swLat: Double?,
    val swLng: Double?,
    val neLat: Double?,
    val neLng: Double?,
) {
    companion object {
        // 테스트 등에서 "not set" 위치를 쉽게 생성하기 위한 헬퍼
        fun notSet() = TrashSpotLocation(null, null, null, null)
    }
}

fun TrashSpotLocation.isNotSet(): Boolean = swLat != null && swLng != null && neLat != null && neLng != null
