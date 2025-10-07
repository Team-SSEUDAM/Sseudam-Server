package com.sseudam.fixture.report

import com.navercorp.fixturemonkey.kotlin.setExp
import com.sseudam.common.Address
import com.sseudam.common.GeoJson
import com.sseudam.common.Region
import com.sseudam.common.S3ImageUrl
import com.sseudam.presentation.v1.report.request.ReportValidationRequest
import com.sseudam.presentation.v1.report.request.SpotReportCreateRequest
import com.sseudam.report.ReportStatus
import com.sseudam.report.ReportType
import com.sseudam.report.SpotReport
import com.sseudam.test.helper.fixtureBuilder
import com.sseudam.trashspot.TrashType
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
        }

    val reportValidationRequest =
        fixtureBuilder<ReportValidationRequest> {
            setExp(ReportValidationRequest::name, "테스트 쓰레기통")
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
            setExp(SpotReport.Detail::createdAt, LocalDateTime.now())
        }

    val s3ImageUrl =
        fixtureBuilder<S3ImageUrl> {
            setExp(S3ImageUrl::presignedUrl, "https://example.com/presigned-url")
        }

    private fun randomPoint() =
        GeoJson.Point(
            listOf(
                Arbitraries.doubles().between(126.0, 128.0).sample(),
                Arbitraries.doubles().between(37.0, 38.0).sample(),
            ),
        )
}
