package com.sseudam.trashspot

import com.sseudam.trashspot.image.TrashSpotImage
import com.sseudam.user.UserProfile

data class TrashSpotDetail(
    val trashSpot: TrashSpot.Info,
    val image: TrashSpotImage.Info?,
    val user: UserProfile?,
    val visitedCount: Long,
)
