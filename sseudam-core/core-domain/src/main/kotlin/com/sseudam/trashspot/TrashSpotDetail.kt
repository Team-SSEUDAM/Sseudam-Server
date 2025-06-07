package com.sseudam.trashspot

import com.sseudam.trashspot.image.TrashSpotImage
import com.sseudam.user.UserProfile

data class TrashSpotDetail(
    val trashSpot: TrashSpot,
    val image: TrashSpotImage?,
    val user: UserProfile?,
)
