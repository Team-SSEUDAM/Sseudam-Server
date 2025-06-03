package com.sseudam.support

interface CacheRepository {
    fun get(key: String): String?

    fun put(
        key: String,
        value: String,
        ttl: Long,
    )

    fun delete(key: String)
}
