package com.sseudam.storage.db.core.trashspot.image

import com.sseudam.storage.db.core.support.BaseEntity
import com.sseudam.trashspot.image.TrashSpotImage
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "t_trash_spot_image")
class TrashSpotImageEntity(
    val trashSpotId: Long,
    val imageUrl: String,
) : BaseEntity() {
    constructor(createTrashSpotImage: TrashSpotImage.Create) : this(
        createTrashSpotImage.trashSpotId,
        createTrashSpotImage.imageUrl,
    )

    fun toTrashSpotImage(): TrashSpotImage.Info =
        TrashSpotImage.Info(
            id = id!!,
            trashSpotId = trashSpotId,
            imageUrl = imageUrl,
        )
}
