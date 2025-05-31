package com.sseudam.storage.db.core.trashspot

import com.sseudam.common.Address
import com.sseudam.storage.db.core.support.BaseEntity
import com.sseudam.support.geo.Region
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
    val description: String,
    @Enumerated(value = EnumType.STRING)
    @Column(columnDefinition = "varchar(20)")
    val region: Region,
    @Embedded
    var address: Address,
    @Column(columnDefinition = "geometry(Point, 4326)")
    val point: Point,
    @Enumerated(value = EnumType.STRING)
    @Column(columnDefinition = "varchar(20)")
    val trashType: TrashType,
) : BaseEntity() {
    fun toTrashSpot(): TrashSpot =
        TrashSpot(
            id = id!!,
            description = description,
            region = region,
            address = address,
            point = point,
            trashType = trashType,
            updatedAt = updatedAt,
        )
}
