package com.sseudam.fixture.history

import com.navercorp.fixturemonkey.kotlin.setExp
import com.sseudam.common.Address
import com.sseudam.common.GeoJson
import com.sseudam.common.TrashType
import com.sseudam.history.dto.HistoryStatus
import com.sseudam.history.dto.SpotActionType
import com.sseudam.history.dto.SpotHistory
import com.sseudam.test.helper.fixtureBuilder
import com.sseudam.test.helper.fixtureBuilders
import net.jqwik.api.Arbitraries

object HistoryFixture {
    private val DEFAULT_ADDRESS = Address("강남구", "서울시 강남구 강남동 1-4")
    private val DEFAULT_POINT = GeoJson.Point(listOf(126.977969, 37.566535))

    val spotHistoryInfo =
        fixtureBuilder<SpotHistory.Info> {
            setExp(SpotHistory.Info::id, 1L)
            setExp(SpotHistory.Info::spotId, 1L)
            setExp(SpotHistory.Info::userId, 1L)
            setExp(SpotHistory.Info::point, DEFAULT_POINT)
            setExp(SpotHistory.Info::spotName, "테스트 쓰레기통")
            setExp(SpotHistory.Info::address, DEFAULT_ADDRESS)
            setExp(SpotHistory.Info::trashType, TrashType.GENERAL)
            setExp(SpotHistory.Info::imageUrl, "https://example.com/image.jpg")
            setExp(SpotHistory.Info::status, HistoryStatus.WAITING)
            setExp(SpotHistory.Info::actionType, SpotActionType.REPORT)
        }

    val spotHistoryInfos =
        fixtureBuilders<SpotHistory.Info>(
            block = {
                setExp(SpotHistory.Info::userId, 1L)
                setExp(SpotHistory.Info::point, DEFAULT_POINT)
                setExp(SpotHistory.Info::spotName, Arbitraries.strings().ofMinLength(1).ofMaxLength(50))
                setExp(SpotHistory.Info::address, DEFAULT_ADDRESS)
                setExp(SpotHistory.Info::trashType, Arbitraries.of(TrashType.entries))
                setExp(SpotHistory.Info::imageUrl, Arbitraries.strings().ofMaxLength(255))
                setExp(SpotHistory.Info::status, Arbitraries.of(HistoryStatus.entries))
                setExp(SpotHistory.Info::actionType, Arbitraries.of(SpotActionType.entries))
            },
            size = 3,
        )
}
