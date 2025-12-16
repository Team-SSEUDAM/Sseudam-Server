package com.sseudam.docs.support

import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification
import tools.jackson.module.kotlin.jacksonMapperBuilder

object MockMvcRequestSpecExtension {
    fun MockMvcRequestSpecification.bodyAsString(entity: Any): MockMvcRequestSpecification {
        val objectMapper = jacksonMapperBuilder().build()
        return this.body(objectMapper.writeValueAsString(entity))
    }
}
