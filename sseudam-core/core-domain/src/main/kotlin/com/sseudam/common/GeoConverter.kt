package com.sseudam.common

import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point
import org.locationtech.jts.geom.PrecisionModel
import org.springframework.stereotype.Component

@Component
class GeoConverter {
    companion object {
        private val GEOMETRY_FACTORY = GeometryFactory(PrecisionModel(), 4326)
    }

    fun geoJsonPointToJtsPoint(point: GeoJson.Point): Point {
        val coordinate = Coordinate(point.coordinates[0], point.coordinates[1])
        return GEOMETRY_FACTORY.createPoint(coordinate)
    }
}
