package com.sseudam.domain.trashspot

import com.sseudam.DevelopTest
import com.sseudam.common.Address
import com.sseudam.common.GeoConverter
import com.sseudam.support.geo.GeoJson
import com.sseudam.support.geo.Region
import com.sseudam.trashspot.TrashSpot
import com.sseudam.trashspot.TrashSpotAppender
import com.sseudam.trashspot.TrashSpotLocation
import com.sseudam.trashspot.TrashSpotReader
import com.sseudam.trashspot.TrashSpotService
import com.sseudam.trashspot.TrashSpotUpdater
import com.sseudam.trashspot.TrashSpotValidator
import com.sseudam.trashspot.TrashType
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.mockk.every
import io.mockk.mockk

@DevelopTest
class TrashSpotServiceTest :
    DescribeSpec({
        val reader: TrashSpotReader = mockk()
        val appender: TrashSpotAppender = mockk()
        val updater: TrashSpotUpdater = mockk(relaxed = true)
        val validator: TrashSpotValidator = mockk(relaxed = true)
        val geoConverter: GeoConverter = mockk(relaxed = true)

        val service = TrashSpotService(reader, appender, updater, validator, geoConverter)

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
