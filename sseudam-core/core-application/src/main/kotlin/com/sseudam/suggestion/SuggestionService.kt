package com.sseudam.suggestion

import com.sseudam.common.S3ImageUrl
import com.sseudam.suggestion.command.CancelSuggestionCommand
import com.sseudam.suggestion.component.SuggestionAppender
import com.sseudam.suggestion.component.SuggestionReader
import com.sseudam.suggestion.component.SuggestionUpdater
import com.sseudam.suggestion.component.SuggestionValidator
import com.sseudam.suggestion.event.SuggestionUpdateEvent
import com.sseudam.suggestion.result.CreateSpotSuggestionResult
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import com.sseudam.support.page.OffsetPageRequest
import com.sseudam.support.page.Page
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point
import org.locationtech.jts.geom.PrecisionModel
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SuggestionService(
    private val suggestionAppender: SuggestionAppender,
    private val suggestionReader: SuggestionReader,
    private val suggestionValidator: SuggestionValidator,
    private val suggestionUpdater: SuggestionUpdater,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    companion object {
        private val GEOMETRY_FACTORY = GeometryFactory(PrecisionModel(), 4326)
    }

    fun append(
        create: SpotSuggestion.Create,
        uploadUrl: S3ImageUrl,
    ): CreateSpotSuggestionResult {
        val point =
            GEOMETRY_FACTORY.createPoint(
                Coordinate(create.longitude, create.latitude),
            )
        suggestionValidator.verifyPoint(point)

        val spotSuggestion = suggestionAppender.append(uploadUrl.imageUrl, create)

        return CreateSpotSuggestionResult(
            suggestionInfo = spotSuggestion,
            uploadUrl = uploadUrl,
        )
    }

    fun appendReject(
        suggestionId: Long,
        reason: String?,
    ) = suggestionAppender.appendReject(suggestionId, reason)

    fun findAllSpotSuggestionByUser(userId: Long): List<SpotSuggestion.Info> = suggestionReader.readAllByUser(userId)

    fun findSpotSuggestionByPoint(point: Point): SpotSuggestion.Info? = suggestionReader.readByPoint(point)

    fun findSpotSuggestionByPointAndStatus(
        point: Point,
        status: SuggestionStatus,
    ): SpotSuggestion.Info? = suggestionReader.readByPointAndStatus(point, status)

    fun findSuggestionsBy(
        offsetPageRequest: OffsetPageRequest,
        searchStatus: SuggestionStatus?,
    ): Page<SpotSuggestion.Detail> = suggestionReader.readAllBy(offsetPageRequest, searchStatus)

    fun findSpotSuggestionById(suggestionId: Long): SpotSuggestion.Detail {
        val suggestion = suggestionReader.readBy(suggestionId)
        val rejectSuggestion = suggestionReader.readRejectBySuggestionId(suggestionId)
        return SpotSuggestion.Detail.of(suggestion, rejectSuggestion)
    }

    @Transactional
    fun updateStatus(
        suggestionId: Long,
        status: SuggestionStatus,
        reason: String?,
    ): SpotSuggestion.Info =
        suggestionUpdater
            .update(suggestionId, status)
            .also {
                applicationEventPublisher.publishEvent(
                    SuggestionUpdateEvent(
                        suggestion = it,
                        reason = reason,
                    ),
                )
            }

    fun validateSpotSuggestionName(name: String) {
        if (suggestionReader.existsByName(name)) {
            throw ErrorException(ErrorType.DUPLICATE_SPOT_NAME)
        }
    }

    fun cancel(command: CancelSuggestionCommand) {
        val suggestion = suggestionReader.readBy(command.suggestionId)
        suggestionValidator.verifySuggestion(command.userId, suggestion)
        suggestionUpdater.cancel(command.suggestionId)
    }
}
