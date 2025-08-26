package com.sseudam.fixture.trashspot

import com.navercorp.fixturemonkey.kotlin.set
import com.navercorp.fixturemonkey.kotlin.setExp
import com.sseudam.common.Address
import com.sseudam.support.geo.GeoJson
import com.sseudam.support.geo.Region
import com.sseudam.test.helper.fixtureBuilder
import com.sseudam.test.helper.fixtureBuilders
import com.sseudam.trashspot.TrashSpot
import com.sseudam.trashspot.TrashType
import net.jqwik.api.Arbitraries

object TrashSpotFixture {
    val infos =
        fixtureBuilders<TrashSpot.Info>(
            block = {
                set(TrashSpot.Info::name, Arbitraries.strings().ofMaxLength(50).sample())
                setExp(TrashSpot.Info::region, Region.entries.filter { it != Region.UNKNOWN }.random())
                setExp(TrashSpot.Info::trashType, TrashType.entries.random())
                setExp(
                    TrashSpot.Info::address,
                    Address(
                        city = Arbitraries.strings().ofMaxLength(20).sample(),
                        site = Arbitraries.strings().ofMaxLength(100).sample(),
                    ),
                )
                setExp(
                    TrashSpot.Info::point,
                    GeoJson.Point(
                        listOf(
                            Arbitraries.doubles().between(126.0, 128.0).sample(),
                            Arbitraries.doubles().between(37.0, 38.0).sample(),
                        ),
                    ),
                )
            },
            size = 5,
        )

    val info =
        fixtureBuilder<TrashSpot.Info> {
            set(TrashSpot.Info::name, Arbitraries.strings().ofMaxLength(50).sample())
            setExp(TrashSpot.Info::region, Region.SEOUL)
            setExp(TrashSpot.Info::trashType, TrashType.GENERAL)
            setExp(
                TrashSpot.Info::address,
                Address(
                    city = "은평구",
                    site = "은평구 어디APT",
                ),
            )
            setExp(TrashSpot.Info::point, GeoJson.Point(listOf(126.9201620274132, 37.643421545524795)))
        }

    fun createTrashSpotInfos(size: Int = 5): List<TrashSpot.Create> =
        fixtureBuilders<TrashSpot.Create>(
            block = {
                set(TrashSpot.Create::name, Arbitraries.strings().ofMaxLength(50).sample())
                setExp(TrashSpot.Create::region, Region.entries.filter { it != Region.UNKNOWN }.random())
                setExp(TrashSpot.Create::trashType, TrashType.entries.random())
                setExp(
                    TrashSpot.Create::address,
                    Address(
                        city = Arbitraries.strings().ofMaxLength(20).sample(),
                        site = Arbitraries.strings().ofMaxLength(100).sample(),
                    ),
                )
                setExp(
                    TrashSpot.Create::point,
                    GeoJson.Point(
                        listOf(
                            Arbitraries.doubles().between(126.0, 128.0).sample(),
                            Arbitraries.doubles().between(37.0, 38.0).sample(),
                        ),
                    ),
                )
            },
            size = size,
        )

    fun createTrashSpotInfoWithId(id: Long): TrashSpot.Create =
        fixtureBuilder<TrashSpot.Create> {
            set("name", Arbitraries.strings().ofMaxLength(50))
            setExp(TrashSpot.Create::region, Region.SEOUL)
            setExp(TrashSpot.Create::trashType, TrashType.GENERAL)
            setExp(
                TrashSpot.Create::address,
                Address(
                    city = "은평구",
                    site = "은평구 어디APT",
                ),
            )
            setExp(TrashSpot.Create::point, GeoJson.Point(listOf(126.9201620274132, 37.643421545524795)))
        }
}
