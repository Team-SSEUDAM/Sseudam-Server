package com.sseudam.fixture.report

import com.navercorp.fixturemonkey.kotlin.setExp
import com.sseudam.common.Address
import com.sseudam.common.GeoJson
import com.sseudam.common.Region
import com.sseudam.common.S3ImageUrl
import com.sseudam.pet.PetPointAction
import com.sseudam.presentation.v1.report.request.ReportCancelRequest
import com.sseudam.presentation.v1.report.request.ReportValidationRequest
import com.sseudam.presentation.v1.report.request.SpotReportCreateRequest
import com.sseudam.report.ReportStatus
import com.sseudam.report.ReportType
import com.sseudam.report.SpotReport
import com.sseudam.report.command.CancelReportCommand
import com.sseudam.report.command.UpdateReportCommand
import com.sseudam.report.event.SpotReportCreatedEvent
import com.sseudam.report.event.SpotReportUpdateEvent
import com.sseudam.report.reject.ReportReject
import com.sseudam.test.helper.fixtureBuilder
import com.sseudam.common.TrashType
import com.sseudam.trashspot.image.TrashSpotImage
import net.jqwik.api.Arbitraries
import java.time.LocalDateTime

object ReportFixture {
    val spotReportCreateRequest =
        fixtureBuilder<SpotReportCreateRequest> {
            setExp(SpotReportCreateRequest::reportType, ReportType.POINT)
            setExp(SpotReportCreateRequest::latitude, 37.566535)
            setExp(SpotReportCreateRequest::longitude, 126.977969)
            setExp(SpotReportCreateRequest::spotName, "우리집 앞")
            setExp(SpotReportCreateRequest::region, Region.SEOUL)
            setExp(SpotReportCreateRequest::city, "강남구")
            setExp(SpotReportCreateRequest::site, "서울시 강남구 강남동 1-4")
            setExp(SpotReportCreateRequest::trashType, TrashType.GENERAL)
            setExp(SpotReportCreateRequest::reason, "잘못된 위치에 있어요")
        }

    val reportValidationRequest =
        fixtureBuilder<ReportValidationRequest> {
            setExp(ReportValidationRequest::name, "테스트 쓰레기통")
        }

    val reportCancelRequest =
        fixtureBuilder<ReportCancelRequest> {
            setExp(ReportCancelRequest::reportId, 1L)
        }

    val cancelReportCommand =
        fixtureBuilder<CancelReportCommand> {
            setExp(CancelReportCommand::userId, 1L)
            setExp(CancelReportCommand::reportId, 1L)
        }

    val spotReportInfo =
        fixtureBuilder<SpotReport.Info> {
            setExp(SpotReport.Info::id, 1L)
            setExp(SpotReport.Info::spotId, 1L)
            setExp(SpotReport.Info::userId, 1L)
            setExp(SpotReport.Info::reportType, ReportType.POINT)
            setExp(SpotReport.Info::point, randomPoint())
            setExp(SpotReport.Info::spotName, "테스트 쓰레기통")
            setExp(SpotReport.Info::region, Region.SEOUL)
            setExp(SpotReport.Info::address, Address("강남구", "서울시 강남구 강남동 1-4"))
            setExp(SpotReport.Info::trashType, TrashType.GENERAL)
            setExp(SpotReport.Info::imageUrl, "https://example.com/image.jpg")
            setExp(SpotReport.Info::status, ReportStatus.WAITING)
            setExp(SpotReport.Info::reason, "잘못된 위치에 있어요")
            setExp(SpotReport.Info::createdAt, LocalDateTime.now())
        }

    val emptySpotReportInfo =
        fixtureBuilder<SpotReport.Info> {
            setExp(SpotReport.Info::id, 2L)
            setExp(SpotReport.Info::spotId, 1L)
            setExp(SpotReport.Info::userId, 1L)
            setExp(SpotReport.Info::reportType, ReportType.EMPTY_SPOT)
            setExp(SpotReport.Info::point, randomPoint())
            setExp(SpotReport.Info::spotName, "테스트 쓰레기통")
            setExp(SpotReport.Info::region, Region.SEOUL)
            setExp(SpotReport.Info::address, Address("강남구", "서울시 강남구 강남동 1-4"))
            setExp(SpotReport.Info::trashType, TrashType.GENERAL)
            setExp(SpotReport.Info::imageUrl, "https://example.com/image.jpg")
            setExp(SpotReport.Info::status, ReportStatus.WAITING)
            setExp(SpotReport.Info::reason, null)
            setExp(SpotReport.Info::createdAt, LocalDateTime.now())
        }

    val etcReportInfo =
        fixtureBuilder<SpotReport.Info> {
            setExp(SpotReport.Info::id, 3L)
            setExp(SpotReport.Info::spotId, 1L)
            setExp(SpotReport.Info::userId, 1L)
            setExp(SpotReport.Info::reportType, ReportType.ETC)
            setExp(SpotReport.Info::point, randomPoint())
            setExp(SpotReport.Info::spotName, "테스트 쓰레기통")
            setExp(SpotReport.Info::region, Region.SEOUL)
            setExp(SpotReport.Info::address, Address("강남구", "서울시 강남구 강남동 1-4"))
            setExp(SpotReport.Info::trashType, TrashType.GENERAL)
            setExp(SpotReport.Info::imageUrl, "https://example.com/image.jpg")
            setExp(SpotReport.Info::status, ReportStatus.WAITING)
            setExp(SpotReport.Info::reason, "기타 사유입니다")
            setExp(SpotReport.Info::createdAt, LocalDateTime.now())
        }

    val spotReportDetail =
        fixtureBuilder<SpotReport.Detail> {
            setExp(SpotReport.Detail::id, 1L)
            setExp(SpotReport.Detail::spotId, 1L)
            setExp(SpotReport.Detail::userId, 1L)
            setExp(SpotReport.Detail::reportType, ReportType.POINT)
            setExp(SpotReport.Detail::point, randomPoint())
            setExp(SpotReport.Detail::spotName, "테스트 쓰레기통")
            setExp(SpotReport.Detail::region, Region.SEOUL)
            setExp(SpotReport.Detail::address, Address("강남구", "서울시 강남구 강남동 1-4"))
            setExp(SpotReport.Detail::trashType, TrashType.GENERAL)
            setExp(SpotReport.Detail::imageUrl, "https://example.com/image.jpg")
            setExp(SpotReport.Detail::status, ReportStatus.WAITING)
            setExp(SpotReport.Detail::rejectReason, null)
            setExp(SpotReport.Detail::reason, "잘못된 위치에 있어요")
            setExp(SpotReport.Detail::createdAt, LocalDateTime.now())
        }

    val s3ImageUrl =
        fixtureBuilder<S3ImageUrl> {
            setExp(S3ImageUrl::presignedUrl, "https://example.com/presigned-url")
            setExp(S3ImageUrl::imageUrl, "https://example.com/image.jpg")
        }

    val spotReportCreate =
        fixtureBuilder<SpotReport.Create> {
            setExp(SpotReport.Create::spotId, 1L)
            setExp(SpotReport.Create::userId, 1L)
            setExp(SpotReport.Create::reportType, ReportType.POINT)
            setExp(SpotReport.Create::latitude, 37.566535)
            setExp(SpotReport.Create::longitude, 126.977969)
            setExp(SpotReport.Create::spotName, "테스트 쓰레기통")
            setExp(SpotReport.Create::region, Region.SEOUL)
            setExp(SpotReport.Create::city, "강남구")
            setExp(SpotReport.Create::site, "서울시 강남구 강남동 1-4")
            setExp(SpotReport.Create::trashType, TrashType.GENERAL)
            setExp(SpotReport.Create::reason, "잘못된 위치에 있어요")
        }

    val trashSpotImageInfo =
        fixtureBuilder<TrashSpotImage.Info> {
            setExp(TrashSpotImage.Info::id, 1L)
            setExp(TrashSpotImage.Info::trashSpotId, 1L)
            setExp(TrashSpotImage.Info::imageUrl, "https://example.com/spot-image.jpg")
            setExp(TrashSpotImage.Info::updatedAt, LocalDateTime.now())
        }

    val spotReportCreatedEvent =
        fixtureBuilder<SpotReportCreatedEvent> {
            setExp(SpotReportCreatedEvent::spotReport, spotReportInfo)
            setExp(SpotReportCreatedEvent::userId, 1L)
            setExp(SpotReportCreatedEvent::petPointAction, PetPointAction.REPORT)
        }

    val updateReportCommand =
        fixtureBuilder<UpdateReportCommand> {
            setExp(UpdateReportCommand::reportId, 1L)
            setExp(UpdateReportCommand::spotId, 1L)
            setExp(UpdateReportCommand::status, ReportStatus.APPROVE)
            setExp(UpdateReportCommand::rejectReason, null)
        }

    val spotReportUpdateEvent =
        fixtureBuilder<SpotReportUpdateEvent> {
            setExp(SpotReportUpdateEvent::report, spotReportInfo)
            setExp(SpotReportUpdateEvent::reason, null)
        }

    val reportRejectInfo =
        fixtureBuilder<ReportReject.Info> {
            setExp(ReportReject.Info::reportId, 1L)
            setExp(ReportReject.Info::reason, "부적절한 위치")
            setExp(ReportReject.Info::createdAt, LocalDateTime.now())
        }

    private fun randomPoint() =
        GeoJson.Point(
            listOf(
                Arbitraries.doubles().between(126.0, 128.0).sample(),
                Arbitraries.doubles().between(37.0, 38.0).sample(),
            ),
        )
}
