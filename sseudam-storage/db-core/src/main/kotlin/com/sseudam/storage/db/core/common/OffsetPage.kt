package com.sseudam.storage.db.core.common

import org.springframework.data.domain.Page

class OffsetPage<T>(
    val page: Int,
    val size: Int,
    val totalCount: Long,
    val data: List<T>,
    val hasNext: Boolean,
) {
    constructor(page: Page<T>) : this(
        page = page.number,
        size = page.size,
        data = page.content,
        totalCount = page.totalElements,
        hasNext = page.hasNext(),
    )

    fun <R> map(mapper: (T) -> R): OffsetPage<R> =
        OffsetPage(
            page = page,
            size = size,
            data = data.map(mapper),
            totalCount = totalCount,
            hasNext = hasNext,
        )
}
