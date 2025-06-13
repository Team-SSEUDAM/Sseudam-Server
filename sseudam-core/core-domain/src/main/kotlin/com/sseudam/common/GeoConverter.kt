package com.sseudam.common

import com.sseudam.support.geo.GeoJson
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.springframework.stereotype.Component

@Component
class GeoConverter {
    companion object {
        private val GEOMETRY_FACTORY = GeometryFactory()
    }

    fun geoJsonPointToJtsPoint(point: GeoJson.Point): org.locationtech.jts.geom.Point {
        val coordinate = Coordinate(point.coordinates[0], point.coordinates[1])
        return GEOMETRY_FACTORY.createPoint(coordinate)
    }
}
