package com.sseudam.support

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

@Component
class Cache(
    _cacheAdvice: CacheAdvice,
) {
    init {
        cacheAdvice = _cacheAdvice
    }

    companion object {
        const val TTL_1_DAY = 24 * 60L
        private lateinit var cacheAdvice: CacheAdvice

        fun <T> cache(
            ttl: Long,
            key: String,
            typeReference: TypeReference<T>,
            function: () -> T,
        ): T =
            if (Companion::cacheAdvice.isInitialized) {
                cacheAdvice.invoke(ttl, key, typeReference, function)
            } else {
                function()
            }

        fun <T> put(
            key: String,
            value: T,
            ttl: Long,
        ) {
            if (Companion::cacheAdvice.isInitialized) {
                cacheAdvice.put(key, value, ttl)
            }
        }

        fun delete(key: String) {
            if (Companion::cacheAdvice.isInitialized) {
                cacheAdvice.delete(key)
            }
        }
    }
}

@Component
class CacheAdvice(
    private val cacheRepository: CacheRepository,
    private val objectMapper: ObjectMapper,
) {
    fun <T> invoke(
        ttl: Long,
        key: String,
        typeReference: TypeReference<T>,
        function: () -> T,
    ): T {
        val cached = cacheRepository.get(key)
        if (cached != null) {
            return objectMapper.readValue(cached, typeReference)
        }

        val result = function()

        if (result != null) {
            val serialized = objectMapper.writeValueAsString(result)
            cacheRepository.put(key, serialized, ttl)
        }
        return result
    }

    fun <T> put(
        key: String,
        value: T,
        ttl: Long,
    ) = cacheRepository.put(key, objectMapper.writeValueAsString(value), ttl)

    fun delete(key: String) = cacheRepository.delete(key)
}
