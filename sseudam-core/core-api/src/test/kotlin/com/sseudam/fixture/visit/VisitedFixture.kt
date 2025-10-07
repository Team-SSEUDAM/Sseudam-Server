package com.sseudam.fixture.visit

import com.navercorp.fixturemonkey.kotlin.setExp
import com.sseudam.test.helper.fixtureBuilder
import com.sseudam.visit.SpotVisited
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
}
