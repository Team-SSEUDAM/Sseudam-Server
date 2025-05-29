package com.sseudam.trashspot.image

/**
 * TrashSpotImage
 * @property id 이미지 id
 * @property trashSpotId 쓰레기통 id
 * @property imageUrl 이미지 url
 */
data class TrashSpotImage(
    val id: Long,
    val trashSpotId: Long,
    val imageUrl: String,
)
