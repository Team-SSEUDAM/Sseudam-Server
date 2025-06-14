package com.sseudam.storage.db.core.suggestion

import com.sseudam.common.Address
import com.sseudam.storage.db.core.support.BaseEntity
import com.sseudam.suggestion.SpotSuggestion
import com.sseudam.suggestion.SuggestionStatus
import com.sseudam.support.geo.GeoJson
import com.sseudam.support.geo.Region
import com.sseudam.trashspot.TrashType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import org.locationtech.jts.geom.Point

@Entity
@Table(name = "t_spot_suggestion")
class SpotSuggestionEntity(
    val userId: Long,
    @Column(columnDefinition = "varchar(50)")
    val spotName: String,
    @Column(columnDefinition = "geometry(Point, 4326)")
    val point: Point,
    @Enumerated(value = EnumType.STRING)
    @Column(columnDefinition = "varchar(15)")
    val region: Region,
    val address: Address,
    @Enumerated(value = EnumType.STRING)
    @Column(columnDefinition = "varchar(15)")
    val trashType: TrashType,
    val imageUrl: String,
    @Enumerated(value = EnumType.STRING)
    @Column(columnDefinition = "varchar(15)")
    var status: SuggestionStatus,
) : BaseEntity() {
    constructor(
        imageUrl: String,
        point: Point,
        createSpotSuggestion: SpotSuggestion.Create,
    ) : this(
        userId = createSpotSuggestion.userId,
        spotName = createSpotSuggestion.spotName,
        point = point,
        region = createSpotSuggestion.region,
        address =
            Address(
                city = createSpotSuggestion.city,
                site = createSpotSuggestion.site,
            ),
        trashType = createSpotSuggestion.trashType,
        imageUrl = imageUrl,
        status = SuggestionStatus.WAITING,
    )

    fun toSpotSuggestion(): SpotSuggestion.Info =
        SpotSuggestion.Info(
            id = id!!,
            userId = userId,
            spotName = spotName,
            point = GeoJson.Point(listOf(point.x, point.y)),
            region = region,
            address = address,
            trashType = trashType,
            status = status,
            imageUrl = imageUrl,
            createdAt = createdAt,
        )

    fun updateStatus(status: SuggestionStatus): SpotSuggestionEntity {
        this.status = status
        return this
    }
}
