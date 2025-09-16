package com.sseudam.storage.db.core.trashspot

import com.sseudam.common.Address
import com.sseudam.common.GeoJson
import com.sseudam.common.Region
import com.sseudam.storage.db.core.support.BaseEntity
import com.sseudam.trashspot.TrashSpot
import com.sseudam.trashspot.TrashType
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import org.locationtech.jts.geom.Point

@Entity
@Table(name = "t_trash_spot")
class TrashSpotEntity(
    @Column(length = 100)
    var name: String,
    @Enumerated(value = EnumType.STRING)
    @Column(length = 20)
    var region: Region,
    @Embedded
    var address: Address,
    @Column
    var point: Point,
    @Enumerated(value = EnumType.STRING)
    @Column(length = 20)
    var trashType: TrashType,
) : BaseEntity() {
    constructor(createTrashSpot: TrashSpot.Create) : this(
        name = createTrashSpot.name,
        region = createTrashSpot.region,
        address = createTrashSpot.address,
        point = createTrashSpot.point,
        trashType = createTrashSpot.trashType,
    )

    fun toTrashSpot(): TrashSpot.Info =
        TrashSpot.Info(
            id = id!!,
            name = name,
            region = region,
            address = address,
            point = GeoJson.Point(listOf(point.x, point.y)),
            trashType = trashType,
            updatedAt = updatedAt,
        )

    fun updateName(name: String) {
        this.name = name
    }

    fun updateType(type: TrashType) {
        this.trashType = type
    }

    fun updateLocation(
        region: Region,
        point: Point,
    ) {
        this.region = region
        this.point = point
    }
}
