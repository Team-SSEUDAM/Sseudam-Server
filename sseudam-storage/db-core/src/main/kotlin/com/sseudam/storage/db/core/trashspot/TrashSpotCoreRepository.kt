package com.sseudam.storage.db.core.trashspot

import com.sseudam.storage.db.core.support.findByIdOrElseThrow
import com.sseudam.support.geo.Region
import com.sseudam.support.tx.TxAdvice
import com.sseudam.trashspot.TrashSpot
import com.sseudam.trashspot.TrashSpotLocation
import com.sseudam.trashspot.TrashSpotRepository
import com.sseudam.trashspot.TrashType
import org.locationtech.jts.geom.Point
import org.springframework.stereotype.Repository

@Repository
class TrashSpotCoreRepository(
    private val trashSpotJpaRepository: TrashSpotJpaRepository,
    private val txAdvice: TxAdvice,
) : TrashSpotRepository {
    override fun save(createTrashSpot: TrashSpot.Create): TrashSpot.Info =
        txAdvice.write {
            trashSpotJpaRepository
                .save(
                    TrashSpotEntity(createTrashSpot),
                ).toTrashSpot()
        }

    override fun findAll(): List<TrashSpot.Info> =
        txAdvice.readOnly {
            trashSpotJpaRepository
                .findAll()
                .map { it.toTrashSpot() }
        }

    override fun findAllByRegion(region: Region): List<TrashSpot.Info> =
        txAdvice.readOnly {
            trashSpotJpaRepository
                .findAllByRegion(region)
                .map { it.toTrashSpot() }
        }

    override fun findAllByLocation(location: TrashSpotLocation): List<TrashSpot.Info> =
        txAdvice.readOnly {
            trashSpotJpaRepository
                .findAllByLocation(
                    location.swLat!!,
                    location.swLng!!,
                    location.neLat!!,
                    location.neLng!!,
                ).map { it.toTrashSpot() }
        }

    override fun findAllByLocationAndRegion(
        region: Region,
        location: TrashSpotLocation,
    ): List<TrashSpot.Info> =
        txAdvice.readOnly {
            trashSpotJpaRepository
                .findAllByLocationAndRegion(
                    location.swLat!!,
                    location.swLng!!,
                    location.neLat!!,
                    location.neLng!!,
                    region.name,
                ).map { it.toTrashSpot() }
        }

    override fun findAllByType(type: TrashType): List<TrashSpot.Info> =
        txAdvice.readOnly {
            trashSpotJpaRepository
                .findAllByTrashType(type)
                .map { it.toTrashSpot() }
        }

    override fun findAllByLocationAndType(
        location: TrashSpotLocation,
        type: TrashType,
    ): List<TrashSpot.Info> =
        txAdvice.readOnly {
            trashSpotJpaRepository
                .findAllByLocationAndType(
                    location.swLat!!,
                    location.swLng!!,
                    location.neLat!!,
                    location.neLng!!,
                    type.name,
                ).map { it.toTrashSpot() }
        }

    override fun findById(spotId: Long): TrashSpot.Info =
        txAdvice.readOnly {
            trashSpotJpaRepository
                .findByIdOrElseThrow(spotId)
                .toTrashSpot()
        }

    override fun findAllByIds(spotIds: List<Long>): List<TrashSpot.Info> =
        txAdvice.readOnly {
            trashSpotJpaRepository
                .findAllByIdIn(spotIds)
                .map { it.toTrashSpot() }
        }

    override fun updateName(
        spotId: Long,
        name: String,
    ) = txAdvice.write {
        trashSpotJpaRepository
            .findByIdOrElseThrow(spotId)
            .updateName(name)
    }

    override fun updateType(
        spotId: Long,
        type: TrashType,
    ) = txAdvice.write {
        trashSpotJpaRepository
            .findByIdOrElseThrow(spotId)
            .updateType(type)
    }

    override fun updateLocation(
        spotId: Long,
        point: Point,
    ) {
        txAdvice.write {
            trashSpotJpaRepository
                .findByIdOrElseThrow(spotId)
                .updateLocation(point)
        }
    }
}
