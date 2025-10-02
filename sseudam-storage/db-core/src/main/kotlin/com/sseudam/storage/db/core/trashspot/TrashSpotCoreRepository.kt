package com.sseudam.storage.db.core.trashspot

import com.sseudam.common.Region
import com.sseudam.storage.db.core.support.findByIdOrElseThrow
import com.sseudam.support.tx.Tx
import com.sseudam.trashspot.TrashSpot
import com.sseudam.trashspot.TrashSpotLocation
import com.sseudam.trashspot.TrashSpotRepository
import com.sseudam.trashspot.TrashType
import org.locationtech.jts.geom.Point
import org.springframework.stereotype.Repository

@Repository
class TrashSpotCoreRepository(
    private val trashSpotJpaRepository: TrashSpotJpaRepository,
) : TrashSpotRepository {
    override fun save(createTrashSpot: TrashSpot.Create): TrashSpot.Info =
        Tx.writeable {
            trashSpotJpaRepository
                .save(
                    TrashSpotEntity(createTrashSpot),
                ).toTrashSpot()
        }

    override fun findAll(): List<TrashSpot.Info> =
        Tx.readable {
            trashSpotJpaRepository
                .findAll()
                .map { it.toTrashSpot() }
        }

    override fun findAllByRegion(region: Region): List<TrashSpot.Info> =
        Tx.readable {
            trashSpotJpaRepository
                .findAllByRegion(region)
                .map { it.toTrashSpot() }
        }

    override fun findAllByLocation(location: TrashSpotLocation): List<TrashSpot.Info> =
        Tx.readable {
            // region/type 모두 사용하지 않는 경우에 대한 단일 네이티브 쿼리 사용
            trashSpotJpaRepository
                .findAllByLocationWithOptionalFilters(
                    location.swLat!!,
                    location.swLng!!,
                    location.neLat!!,
                    location.neLng!!,
                    null,
                    null,
                ).map { it.toTrashSpot() }
        }

    override fun findAllByLocationAndRegion(
        region: Region,
        location: TrashSpotLocation,
    ): List<TrashSpot.Info> =
        Tx.readable {
            trashSpotJpaRepository
                .findAllByLocationWithOptionalFilters(
                    location.swLat!!,
                    location.swLng!!,
                    location.neLat!!,
                    location.neLng!!,
                    region.name,
                    null,
                ).map { it.toTrashSpot() }
        }

    override fun findAllByType(type: TrashType): List<TrashSpot.Info> =
        Tx.readable {
            trashSpotJpaRepository
                .findAllByTrashType(type)
                .map { it.toTrashSpot() }
        }

    override fun findAllByLocationAndType(
        location: TrashSpotLocation,
        type: TrashType,
    ): List<TrashSpot.Info> =
        Tx.readable {
            trashSpotJpaRepository
                .findAllByLocationWithOptionalFilters(
                    location.swLat!!,
                    location.swLng!!,
                    location.neLat!!,
                    location.neLng!!,
                    null,
                    type.name,
                ).map { it.toTrashSpot() }
        }

    override fun findById(spotId: Long): TrashSpot.Info =
        Tx.readable {
            trashSpotJpaRepository
                .findByIdOrElseThrow(spotId)
                .toTrashSpot()
        }

    override fun findAllByIds(spotIds: List<Long>): List<TrashSpot.Info> =
        Tx.readable {
            trashSpotJpaRepository
                .findAllByIdIn(spotIds)
                .map { it.toTrashSpot() }
        }

    override fun findBySite(site: String): TrashSpot.Info? =
        Tx.readable {
            trashSpotJpaRepository
                .findByAddressSite(site)
                ?.toTrashSpot()
        }

    override fun findByPoint(point: Point): TrashSpot.Info? =
        Tx.readable {
            trashSpotJpaRepository
                .findByPointAndDeletedAtIsNull(point)
                ?.toTrashSpot()
        }

    override fun updateName(
        spotId: Long,
        name: String,
    ) = Tx.writeable {
        trashSpotJpaRepository
            .findByIdOrElseThrow(spotId)
            .updateName(name)
    }

    override fun updateType(
        spotId: Long,
        type: TrashType,
    ) = Tx.writeable {
        trashSpotJpaRepository
            .findByIdOrElseThrow(spotId)
            .updateType(type)
    }

    override fun updateLocation(
        spotId: Long,
        region: Region,
        point: Point,
    ) {
        Tx.writeable {
            trashSpotJpaRepository
                .findByIdOrElseThrow(spotId)
                .updateLocation(region, point)
        }
    }

    override fun existsByName(name: String): Boolean =
        Tx.readable {
            trashSpotJpaRepository.existsByName(name)
        }
}
