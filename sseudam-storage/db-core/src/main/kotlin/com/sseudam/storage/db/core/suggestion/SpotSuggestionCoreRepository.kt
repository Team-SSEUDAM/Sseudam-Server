package com.sseudam.storage.db.core.suggestion

import com.sseudam.storage.db.core.support.findByIdAndDeletedAtIsNullOrElseThrow
import com.sseudam.storage.db.core.support.findByIdOrElseThrow
import com.sseudam.suggestion.SpotSuggestion
import com.sseudam.suggestion.SuggestionStatus
import com.sseudam.suggestion.repository.SpotSuggestionRepository
import com.sseudam.support.cursor.OffsetPageRequest
import com.sseudam.support.page.Page
import com.sseudam.support.tx.Tx
import org.locationtech.jts.geom.Point
import org.springframework.stereotype.Repository

@Repository
class SpotSuggestionCoreRepository(
    private val spotSuggestionJpaRepository: SpotSuggestionJpaRepository,
    private val spotSuggestionCustomRepository: SpotSuggestionCustomRepository,
) : SpotSuggestionRepository {
    override fun create(
        imageUrl: String,
        point: Point,
        createSpotSuggestion: SpotSuggestion.Create,
    ): SpotSuggestion.Info =
        Tx.writeable {
            spotSuggestionJpaRepository
                .save(
                    SpotSuggestionEntity(imageUrl, point, createSpotSuggestion),
                ).toSpotSuggestion()
        }

    override fun findBy(suggestionId: Long): SpotSuggestion.Info =
        Tx.readable {
            spotSuggestionJpaRepository
                .findByIdAndDeletedAtIsNullOrElseThrow(suggestionId)
                .toSpotSuggestion()
        }

    override fun findAllByUserId(userId: Long): List<SpotSuggestion.Info> =
        Tx.readable {
            spotSuggestionJpaRepository
                .findAllByUserIdAndDeletedAtIsNull(userId)
                .map { it.toSpotSuggestion() }
        }

    override fun findBySite(site: String): SpotSuggestion.Info? =
        Tx.readable {
            spotSuggestionJpaRepository
                .findByAddressSiteAndDeletedAtIsNull(site)
                ?.toSpotSuggestion()
        }

    override fun findByPoint(point: Point): SpotSuggestion.Info? =
        Tx.readable {
            spotSuggestionJpaRepository
                .findByPointAndDeletedAtIsNull(point)
                ?.toSpotSuggestion()
        }

    override fun findByPointAndStatus(
        point: Point,
        status: SuggestionStatus,
    ): SpotSuggestion.Info? =
        Tx.readable {
            spotSuggestionJpaRepository
                .findByPointAndStatusAndDeletedAtIsNull(point, status)
                ?.toSpotSuggestion()
        }

    override fun findAllBy(
        offsetPageRequest: OffsetPageRequest,
        searchStatus: SuggestionStatus?,
    ): Page<SpotSuggestion.Detail> =
        Tx.readable {
            spotSuggestionCustomRepository.findAllBy(offsetPageRequest, searchStatus)
        }

    override fun update(
        suggestionId: Long,
        status: SuggestionStatus,
    ): SpotSuggestion.Info =
        Tx.writeable {
            val suggestion = spotSuggestionJpaRepository.findByIdOrElseThrow(suggestionId)
            return@writeable suggestion.updateStatus(status).toSpotSuggestion()
        }

    override fun cancel(suggestionId: Long) =
        Tx.writeable {
            val suggestion = spotSuggestionJpaRepository.findByIdAndDeletedAtIsNullOrElseThrow(suggestionId)
            suggestion.cancel()
        }

    override fun existsByName(name: String): Boolean =
        Tx.readable {
            spotSuggestionJpaRepository.existsBySpotNameAndDeletedAtIsNull(name)
        }

    override fun deleteBy(suggestionId: Long) =
        Tx.writeable {
            val suggestion = spotSuggestionJpaRepository.findByIdAndDeletedAtIsNullOrElseThrow(suggestionId)
            suggestion.softDelete()
        }
}
