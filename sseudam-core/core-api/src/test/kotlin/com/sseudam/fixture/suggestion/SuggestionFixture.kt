package com.sseudam.fixture.suggestion

import com.navercorp.fixturemonkey.kotlin.setExp
import com.sseudam.common.Address
import com.sseudam.common.GeoJson
import com.sseudam.common.Region
import com.sseudam.common.S3ImageUrl
import com.sseudam.pet.PetPointAction
import com.sseudam.presentation.v1.suggestion.request.SpotSuggestionCreateRequest
import com.sseudam.presentation.v1.suggestion.request.SuggestionCancelRequest
import com.sseudam.presentation.v1.suggestion.request.SuggestionValidationRequest
import com.sseudam.suggestion.SpotSuggestion
import com.sseudam.suggestion.SuggestionStatus
import com.sseudam.suggestion.command.CancelSuggestionCommand
import com.sseudam.suggestion.event.SpotSuggestionCreatedEvent
import com.sseudam.test.helper.fixtureBuilder
import com.sseudam.trashspot.TrashType
import net.jqwik.api.Arbitraries
import java.time.LocalDateTime

object SuggestionFixture {
    val spotSuggestionCreateRequest =
        fixtureBuilder<SpotSuggestionCreateRequest> {
            setExp(SpotSuggestionCreateRequest::spotName, "우리집 앞")
            setExp(SpotSuggestionCreateRequest::latitude, 37.566535)
            setExp(SpotSuggestionCreateRequest::longitude, 126.977969)
            setExp(SpotSuggestionCreateRequest::region, Region.SEOUL)
            setExp(SpotSuggestionCreateRequest::city, "강남구")
            setExp(SpotSuggestionCreateRequest::site, "서울시 강남구 강남동 1-4")
            setExp(SpotSuggestionCreateRequest::trashType, TrashType.GENERAL)
        }

    val suggestionValidationRequest =
        fixtureBuilder<SuggestionValidationRequest> {
            setExp(SuggestionValidationRequest::name, "테스트 쓰레기통")
        }

    val suggestionCancelRequest =
        fixtureBuilder<SuggestionCancelRequest> {
            setExp(SuggestionCancelRequest::suggestionId, 1L)
        }

    val cancelSuggestionCommand =
        fixtureBuilder<CancelSuggestionCommand> {
            setExp(CancelSuggestionCommand::suggestionId, 1L)
            setExp(CancelSuggestionCommand::userId, 1L)
        }

    val spotSuggestionInfo =
        fixtureBuilder<SpotSuggestion.Info> {
            setExp(SpotSuggestion.Info::id, 1L)
            setExp(SpotSuggestion.Info::userId, 1L)
            setExp(SpotSuggestion.Info::spotName, "테스트 쓰레기통")
            setExp(SpotSuggestion.Info::point, randomPoint())
            setExp(SpotSuggestion.Info::region, Region.SEOUL)
            setExp(SpotSuggestion.Info::address, Address("강남구", "서울시 강남구 강남동 1-4"))
            setExp(SpotSuggestion.Info::trashType, TrashType.GENERAL)
            setExp(SpotSuggestion.Info::imageUrl, "https://example.com/image.jpg")
            setExp(SpotSuggestion.Info::status, SuggestionStatus.WAITING)
            setExp(SpotSuggestion.Info::createdAt, LocalDateTime.now())
        }

    val spotSuggestionDetail =
        fixtureBuilder<SpotSuggestion.Detail> {
            setExp(SpotSuggestion.Detail::id, 1L)
            setExp(SpotSuggestion.Detail::userId, 1L)
            setExp(SpotSuggestion.Detail::spotName, "테스트 쓰레기통")
            setExp(SpotSuggestion.Detail::point, randomPoint())
            setExp(SpotSuggestion.Detail::region, Region.SEOUL)
            setExp(SpotSuggestion.Detail::address, Address("강남구", "서울시 강남구 강남동 1-4"))
            setExp(SpotSuggestion.Detail::trashType, TrashType.GENERAL)
            setExp(SpotSuggestion.Detail::imageUrl, "https://example.com/image.jpg")
            setExp(SpotSuggestion.Detail::status, SuggestionStatus.WAITING)
            setExp(SpotSuggestion.Detail::rejectReason, null)
            setExp(SpotSuggestion.Detail::createdAt, LocalDateTime.now())
        }

    val s3ImageUrl =
        fixtureBuilder<S3ImageUrl> {
            setExp(S3ImageUrl::presignedUrl, "https://example.com/presigned-url")
        }

    val spotSuggestionCreatedEvent =
        fixtureBuilder<SpotSuggestionCreatedEvent> {
            setExp(SpotSuggestionCreatedEvent::spotSuggestion, spotSuggestionInfo)
            setExp(SpotSuggestionCreatedEvent::userId, 1L)
            setExp(SpotSuggestionCreatedEvent::petPointAction, PetPointAction.SUGGESTION)
        }

    private fun randomPoint() =
        GeoJson.Point(
            listOf(
                Arbitraries.doubles().between(126.0, 128.0).sample(),
                Arbitraries.doubles().between(37.0, 38.0).sample(),
            ),
        )
}
