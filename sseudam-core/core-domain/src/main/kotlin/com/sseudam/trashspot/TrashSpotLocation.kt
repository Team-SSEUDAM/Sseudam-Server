package com.sseudam.trashspot

data class TrashSpotLocation(
    val swLat: Double?,
    val swLng: Double?,
    val neLat: Double?,
    val neLng: Double?,
)

fun TrashSpotLocation.isNotSet(): Boolean = swLat != null && swLng != null && neLat != null && neLng != null
