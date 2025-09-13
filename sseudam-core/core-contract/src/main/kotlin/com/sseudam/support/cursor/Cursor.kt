package com.sseudam.support.cursor

data class Cursor(
    val nextCursor: Long?,
    val size: Int?,
) {
    fun toCursor() =
        Cursor(
            nextCursor = nextCursor ?: DEFAULT_CURSOR,
            size = size ?: DEFAULT_SIZE,
        )

    companion object {
        const val DEFAULT_CURSOR = 0L
        const val DEFAULT_SIZE = 10
    }
}
