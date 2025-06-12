package com.sseudam.support.page

data class Page<T>(
    val content: List<T>,
    val totalCount: Long,
) {
    fun <T> of(
        content: List<T>,
        totalCount: Long,
    ): Page<T> {
        require(totalCount >= 0) { "totalCount must be non-negative" }
        require(totalCount >= content.size) {
            "totalCount ($totalCount) cannot be smaller than content.size (${content.size})"
        }
        val count = if (content.isEmpty()) 0 else totalCount
        return Page(content, count)
    }
}
