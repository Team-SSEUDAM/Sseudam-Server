package com.sseudam.storage.db.core.report

import com.sseudam.common.Address
import com.sseudam.report.ReportStatus
import com.sseudam.report.ReportType
import com.sseudam.report.SpotReport
import com.sseudam.storage.db.core.support.BaseEntity
import com.sseudam.support.geo.GeoJson
import com.sseudam.trashspot.TrashType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import org.locationtech.jts.geom.Point

@Entity
@Table(name = "t_spot_report")
class SpotReportEntity(
    val spotId: Long,
    val userId: Long,
    @Enumerated(value = EnumType.STRING)
    @Column(columnDefinition = "varchar(10)")
    val reportType: ReportType,
    @Column(columnDefinition = "geometry(Point, 4326)")
    val point: Point,
    val address: Address,
    @Column(columnDefinition = "varchar(50)")
    val spotName: String,
    @Enumerated(value = EnumType.STRING)
    @Column(columnDefinition = "varchar(15)")
    val trashType: TrashType,
    val imageUrl: String,
    @Enumerated(value = EnumType.STRING)
    @Column(columnDefinition = "varchar(15)")
    var status: ReportStatus,
) : BaseEntity() {
    constructor(
        imageUrl: String,
        point: Point,
        createSpotReport: SpotReport.Create,
    ) : this(
        spotId = createSpotReport.spotId,
        userId = createSpotReport.userId,
        reportType = createSpotReport.reportType,
        point = point,
        address =
            Address(
                city = createSpotReport.city,
                site = createSpotReport.site,
            ),
        spotName = createSpotReport.spotName,
        trashType = createSpotReport.trashType,
        imageUrl = imageUrl,
        status = ReportStatus.WAITING,
    )

    fun toSpotReport(): SpotReport.Info =
        SpotReport.Info(
            id = id!!,
            spotId = spotId,
            userId = userId,
            reportType = reportType,
            point = GeoJson.Point(listOf(point.x, point.y)),
            spotName = spotName,
            address = address,
            trashType = trashType,
            imageUrl = imageUrl,
            status = status,
            createdAt = createdAt,
        )

    fun updateStatus(status: ReportStatus): SpotReportEntity {
        this.status = status
        return this
    }
}
