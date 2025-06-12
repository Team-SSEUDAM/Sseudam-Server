package com.sseudam.support.page

data class OffsetLimit(
    val offset: Long,
    val limit: Long,
    val type: SortType? = SortType.NEW,
    val sort: Sort? = Sort.DESC,
) {
    companion object {
        fun of(
            offset: Long,
            limit: Long,
            type: SortType? = SortType.NEW,
            sort: Sort? = Sort.DESC,
        ): OffsetLimit = OffsetLimit(offset, limit, type, sort)
    }
}
