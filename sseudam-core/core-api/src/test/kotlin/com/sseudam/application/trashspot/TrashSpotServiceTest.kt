package com.sseudam.application.trashspot

import com.sseudam.DevelopTest
import com.sseudam.common.Address
import com.sseudam.common.GeoConverter
import com.sseudam.common.GeoJson
import com.sseudam.common.Region
import com.sseudam.common.TrashType
import com.sseudam.trashspot.TrashSpot
import com.sseudam.trashspot.TrashSpotService
import com.sseudam.trashspot.component.TrashSpotAppender
import com.sseudam.trashspot.component.TrashSpotReader
import com.sseudam.trashspot.dto.TrashSpotLocation
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.mockk.every
import io.mockk.mockk

@DevelopTest
class TrashSpotServiceTest :
    DescribeSpec({
        val reader: TrashSpotReader = mockk()
        val appender: TrashSpotAppender = mockk()
        val geoConverter: GeoConverter = mockk(relaxed = true)

        val service = TrashSpotService(reader, appender, geoConverter)

        describe("findAll") {
            it("delegates condition lookup to reader and returns results") {
                val expected =
                    listOf(
                        TrashSpot.Info(
                            id = 1L,
                            name = "A",
                            region = Region.SEOUL,
                            trashType = TrashType.GENERAL,
                            address = Address(city = "", site = ""),
                            point = GeoJson.Point(listOf(0.0, 0.0)),
                        ),
                    )
                every { reader.findByCondition(any()) } returns expected

                val actual = service.findAll(region = Region.SEOUL, trashType = null, location = TrashSpotLocation.notSet())

                actual.shouldContainExactly(expected)
            }
        }
    })
