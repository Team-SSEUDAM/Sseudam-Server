package com.sseudam.storage.db.core.suggestion

import com.sseudam.common.Address
import com.sseudam.storage.db.core.support.BaseEntity
import com.sseudam.suggestion.SpotSuggestion
import com.sseudam.suggestion.SuggestionStatus
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
    val point: Point,
    val region: Region,
    val address: Address,
    val trashType: TrashType,
    @Enumerated(value = EnumType.STRING)
    @Column(columnDefinition = "varchar(15)")
    val status: SuggestionStatus,
    val imageUrl: String,
) : BaseEntity() {
    fun toSpotSuggestion(): SpotSuggestion =
        SpotSuggestion(
            id = id!!,
            point = point,
            region = region,
            address = address,
            trashType = trashType,
            status = status,
            imageUrl = imageUrl,
        )
}
