package com.sseudam.storage.db.core.trashspot

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import com.sseudam.support.geo.Region
import com.sseudam.trashspot.TrashType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface TrashSpotJpaRepository :
    JpaRepository<TrashSpotEntity, Long>,
    KotlinJdslJpqlExecutor {
    fun findAllByRegion(region: Region): List<TrashSpotEntity>

    @Query(
        """
        SELECT *
        FROM t_trash_spot
        WHERE ST_Contains(
            ST_MakeEnvelope(:swLng, :swLat, :neLng, :neLat, 4326),
            point
        )
        AND deleted_at IS NULL
        """,
        nativeQuery = true,
    )
    fun findAllByLocation(
        @Param("swLat") swLat: Double,
        @Param("swLng") swLng: Double,
        @Param("neLat") neLat: Double,
        @Param("neLng") neLng: Double,
    ): List<TrashSpotEntity>

    @Query(
        """
        SELECT *
        FROM t_trash_spot
        WHERE ST_Contains(
            ST_MakeEnvelope(:swLng, :swLat, :neLng, :neLat, 4326),
            point
        )
        AND region = :region
        AND deleted_at IS NULL
        """,
        nativeQuery = true,
    )
    fun findAllByLocationAndRegion(
        @Param("swLat") swLat: Double,
        @Param("swLng") swLng: Double,
        @Param("neLat") neLat: Double,
        @Param("neLng") neLng: Double,
        @Param("region") region: String,
    ): List<TrashSpotEntity>

    @Query(
        """
        SELECT *
        FROM t_trash_spot
        WHERE ST_Contains(
            ST_MakeEnvelope(:swLng, :swLat, :neLng, :neLat, 4326),
            point
        )
        AND trash_type = :type
        AND deleted_at IS NULL
        """,
        nativeQuery = true,
    )
    fun findAllByLocationAndType(
        @Param("swLat") swLat: Double,
        @Param("swLng") swLng: Double,
        @Param("neLat") neLat: Double,
        @Param("neLng") neLng: Double,
        @Param("type") type: String,
    ): List<TrashSpotEntity>

    fun findAllByTrashType(type: TrashType): List<TrashSpotEntity>

    fun findAllByIdIn(spotIds: List<Long>): List<TrashSpotEntity>
}
