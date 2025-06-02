package com.sseudam.support.geo

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type",
)
@JsonSubTypes(
    JsonSubTypes.Type(value = GeoJson.Point::class, name = "Point"),
)
sealed class GeoJson {
    @get:JsonIgnore // JSON 직렬화에서 제외
    abstract val type: String

    data class Point(
        val coordinates: List<Double>,
    ) : GeoJson() {
        @JsonIgnore // JSON 직렬화에서 제외
        override val type: String = "Point"
    }
}
