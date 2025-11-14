package com.sseudam.fixture.trashspot

import com.navercorp.fixturemonkey.kotlin.setExp
import com.sseudam.common.Address
import com.sseudam.common.GeoJson
import com.sseudam.common.Region
import com.sseudam.common.TrashType
import com.sseudam.test.helper.fixtureBuilder
import com.sseudam.test.helper.fixtureBuilders
import com.sseudam.trashspot.TrashSpot
import com.sseudam.trashspot.image.TrashSpotImage
import net.jqwik.api.Arbitraries

object TrashSpotFixture {
    private const val DEFAULT_CITY = "은평구"
    private const val DEFAULT_SITE = "은평구 어디APT"
    private const val DEFAULT_LONGITUDE = 126.9201620274132
    private const val DEFAULT_LATITUDE = 37.643421545524795
    private val DEFAULT_REGION = Region.SEOUL
    private val DEFAULT_TRASH_TYPE = TrashType.GENERAL

    private val DEFAULT_ADDRESS = Address(city = DEFAULT_CITY, site = DEFAULT_SITE)
    private val DEFAULT_POINT = GeoJson.Point(listOf(DEFAULT_LONGITUDE, DEFAULT_LATITUDE))

    private fun randomAddress() =
        Address(
            city =
                Arbitraries
                    .strings()
                    .ofMinLength(1)
                    .ofMaxLength(20)
                    .sample(),
            site =
                Arbitraries
                    .strings()
                    .ofMinLength(1)
                    .ofMaxLength(100)
                    .sample(),
        )

    private fun randomPoint() =
        GeoJson.Point(
            listOf(
                Arbitraries.doubles().between(126.0, 128.0).sample(),
                Arbitraries.doubles().between(37.0, 38.0).sample(),
            ),
        )

    val infos =
        fixtureBuilders<TrashSpot.Info>(
            block = {
                set("name", Arbitraries.strings().ofMinLength(1).ofMaxLength(50))
                setExp(TrashSpot.Info::region, Arbitraries.of(Region.entries.filter { it != Region.UNKNOWN }))
                setExp(TrashSpot.Info::trashType, Arbitraries.of(TrashType.entries))
                setExp(TrashSpot.Info::address, randomAddress())
                setExp(TrashSpot.Info::point, randomPoint())
            },
            size = 5,
        )

    val info =
        fixtureBuilder<TrashSpot.Info> {
            set("name", Arbitraries.strings().ofMaxLength(50))
            setExp(TrashSpot.Info::region, DEFAULT_REGION)
            setExp(TrashSpot.Info::trashType, DEFAULT_TRASH_TYPE)
            setExp(TrashSpot.Info::address, DEFAULT_ADDRESS)
            setExp(TrashSpot.Info::point, DEFAULT_POINT)
        }

    val image =
        fixtureBuilder<TrashSpotImage.Info> {
            set("imageUrl", Arbitraries.strings().ofMaxLength(255))
        }

    fun createTrashSpotInfos(size: Int = 5): List<TrashSpot.Create> =
        fixtureBuilders<TrashSpot.Create>(
            block = {
                set("name", Arbitraries.strings().ofMinLength(1).ofMaxLength(50))
                setExp(TrashSpot.Create::region, Arbitraries.of(Region.entries.filter { it != Region.UNKNOWN }))
                setExp(TrashSpot.Create::trashType, Arbitraries.of(TrashType.entries))
                setExp(TrashSpot.Create::address, randomAddress())
                setExp(TrashSpot.Create::point, randomPoint())
            },
            size = size,
        )

    fun createTrashSpotInfoWithId(id: Long): TrashSpot.Create =
        fixtureBuilder<TrashSpot.Create> {
            set("name", Arbitraries.strings().ofMaxLength(50))
            setExp(TrashSpot.Create::region, DEFAULT_REGION)
            setExp(TrashSpot.Create::trashType, DEFAULT_TRASH_TYPE)
            setExp(TrashSpot.Create::address, DEFAULT_ADDRESS)
            setExp(TrashSpot.Create::point, DEFAULT_POINT)
        }
}
