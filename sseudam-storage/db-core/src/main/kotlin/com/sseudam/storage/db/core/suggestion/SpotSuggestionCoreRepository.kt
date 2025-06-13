package com.sseudam.storage.db.core.suggestion

import com.sseudam.storage.db.core.support.findByIdOrElseThrow
import com.sseudam.suggestion.SpotSuggestion
import com.sseudam.suggestion.SpotSuggestionRepository
import com.sseudam.suggestion.SuggestionStatus
import com.sseudam.support.cursor.OffsetPageRequest
import com.sseudam.support.tx.TxAdvice
import org.locationtech.jts.geom.Point
import org.springframework.stereotype.Repository

@Repository
class SpotSuggestionCoreRepository(
    private val spotSuggestionJpaRepository: SpotSuggestionJpaRepository,
    private val spotSuggestionCustomRepository: SpotSuggestionCustomRepository,
    private val txAdvice: TxAdvice,
) : SpotSuggestionRepository {
    override fun create(
        imageUrl: String,
        point: Point,
        createSpotSuggestion: SpotSuggestion.Create,
    ): SpotSuggestion.Info =
        txAdvice.write {
            spotSuggestionJpaRepository
                .save(
                    SpotSuggestionEntity(imageUrl, point, createSpotSuggestion),
                ).toSpotSuggestion()
        }

    override fun findBy(suggestionId: Long): SpotSuggestion.Info =
        txAdvice.readOnly {
            spotSuggestionJpaRepository
                .findByIdOrElseThrow(suggestionId)
                .toSpotSuggestion()
        }

    override fun findAllByUserId(userId: Long): List<SpotSuggestion.Info> =
        txAdvice.readOnly {
            spotSuggestionJpaRepository
                .findAllByUserId(userId)
                .map { it.toSpotSuggestion() }
        }

    override fun findBySite(site: String): SpotSuggestion.Info? =
        txAdvice.readOnly {
            spotSuggestionJpaRepository
                .findByAddressSite(site)
                ?.toSpotSuggestion()
        }

    override fun findAllBy(
        offsetPageRequest: OffsetPageRequest,
        searchStatus: SuggestionStatus?,
    ): List<SpotSuggestion.Info> =
        txAdvice.readOnly {
            spotSuggestionCustomRepository.findAllBy(offsetPageRequest, searchStatus)
        }
}
