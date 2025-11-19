package com.sseudam.fixture.visit

import com.navercorp.fixturemonkey.kotlin.setExp
import com.sseudam.common.Address
import com.sseudam.common.GeoJson
import com.sseudam.common.Region
import com.sseudam.common.TrashType
import com.sseudam.contract.trashspot.TrashSpotDto
import com.sseudam.test.helper.fixtureBuilder
import com.sseudam.trashspot.TrashSpot
import com.sseudam.user.UserProfile
import com.sseudam.visit.SpotVisited
import com.sseudam.visit.result.SpotVisitedResult
import net.jqwik.api.Arbitraries
import java.time.LocalDate
import java.time.LocalDateTime

object VisitedFixture {
    val spotVisitedInfo =
        fixtureBuilder<SpotVisited.Info> {
            setExp(SpotVisited.Info::id, 1L)
            setExp(SpotVisited.Info::userId, 1L)
            setExp(SpotVisited.Info::spotId, 1L)
            setExp(SpotVisited.Info::date, LocalDate.now())
            setExp(SpotVisited.Info::site, "서울특별시 강남구 테헤란로 123")
            setExp(SpotVisited.Info::visitedAt, LocalDateTime.now())
        }

    val spotVisitedCreate =
        fixtureBuilder<SpotVisited.Create> {
            setExp(SpotVisited.Create::userId, 1L)
            setExp(SpotVisited.Create::spotId, 1L)
            setExp(SpotVisited.Create::date, LocalDate.now())
        }

    val spotVisitedResult =
        fixtureBuilder<SpotVisitedResult> {
            setExp(SpotVisitedResult::isToday, true)
            setExp(SpotVisitedResult::visited, spotVisitedInfo)
        }

    val trashSpotInfo =
        fixtureBuilder<TrashSpot.Info> {
            setExp(TrashSpot.Info::id, 1L)
            setExp(TrashSpot.Info::point, randomPoint())
            setExp(TrashSpot.Info::address, Address("강남구", "서울시 강남구 강남동 1-4"))
            setExp(TrashSpot.Info::trashType, TrashType.GENERAL)
            setExp(TrashSpot.Info::suggesterId, 1L)
        }

    val trashSpotDto =
        fixtureBuilder<TrashSpotDto> {
            setExp(TrashSpotDto::id, 1L)
            setExp(TrashSpotDto::name, "테스트 쓰레기통")
            setExp(TrashSpotDto::region, Region.SEOUL)
            setExp(TrashSpotDto::point, randomPoint())
            setExp(TrashSpotDto::address, Address("강남구", "서울시 강남구 강남동 1-4"))
            setExp(TrashSpotDto::trashType, TrashType.GENERAL)
            setExp(TrashSpotDto::suggesterId, 1L)
        }

    val userProfile =
        fixtureBuilder<UserProfile> {
            setExp(UserProfile::id, 1L)
            setExp(UserProfile::nickname, "테스트유저")
        }

    private fun randomPoint() =
        GeoJson.Point(
            listOf(
                Arbitraries.doubles().between(126.0, 128.0).sample(),
                Arbitraries.doubles().between(37.0, 38.0).sample(),
            ),
        )
}
