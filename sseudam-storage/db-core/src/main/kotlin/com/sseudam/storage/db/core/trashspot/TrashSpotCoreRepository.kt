package com.sseudam.storage.db.core.trashspot

import com.sseudam.storage.db.core.support.findByIdOrElseThrow
import com.sseudam.support.geo.Region
import com.sseudam.support.tx.TxAdvice
import com.sseudam.trashspot.TrashSpot
import com.sseudam.trashspot.TrashSpotLocation
import com.sseudam.trashspot.TrashSpotRepository
import org.springframework.stereotype.Repository

@Repository
class TrashSpotCoreRepository(
    private val trashSpotJpaRepository: TrashSpotJpaRepository,
    private val txAdvice: TxAdvice,
) : TrashSpotRepository {
    override fun findAll(): List<TrashSpot> =
        txAdvice.readOnly {
            trashSpotJpaRepository
                .findAll()
                .map { it.toTrashSpot() }
        }

    override fun findAllByRegion(region: Region): List<TrashSpot> =
        txAdvice.readOnly {
            trashSpotJpaRepository
                .findAllByRegion(region)
                .map { it.toTrashSpot() }
        }

    override fun findAllByLocation(location: TrashSpotLocation): List<TrashSpot> =
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
    ): List<TrashSpot> =
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

    override fun findById(spotId: Long): TrashSpot =
        txAdvice.readOnly {
            trashSpotJpaRepository
                .findByIdOrElseThrow(spotId)
                .toTrashSpot()
        }
}
