package com.sseudam.trashspot.result

import com.sseudam.trashspot.TrashSpot
import com.sseudam.trashspot.image.TrashSpotImage
import com.sseudam.user.UserProfile

data class TrashSpotDetail(
    val trashSpot: TrashSpot.Info,
    val image: TrashSpotImage.Info?,
    val user: UserProfile?,
    val visitedCount: Long,
)
