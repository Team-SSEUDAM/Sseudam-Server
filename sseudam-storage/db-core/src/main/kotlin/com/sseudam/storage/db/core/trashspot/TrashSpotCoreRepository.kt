package com.sseudam.storage.db.core.trashspot

import com.sseudam.common.Region
import com.sseudam.storage.db.core.support.findByIdOrElseThrow
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
        txAdvice.readOnly {
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

    override fun findBySite(site: String): TrashSpot.Info? =
        txAdvice.readOnly {
            trashSpotJpaRepository
                .findByAddressSite(site)
                ?.toTrashSpot()
        }

    override fun findByPoint(point: Point): TrashSpot.Info? =
        txAdvice.readOnly {
            trashSpotJpaRepository
                .findByPointAndDeletedAtIsNull(point)
                ?.toTrashSpot()
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
        region: Region,
        point: Point,
    ) {
        txAdvice.write {
            trashSpotJpaRepository
                .findByIdOrElseThrow(spotId)
                .updateLocation(region, point)
        }
    }

    override fun existsByName(name: String): Boolean =
        txAdvice.readOnly {
            trashSpotJpaRepository.existsByName(name)
        }
}
