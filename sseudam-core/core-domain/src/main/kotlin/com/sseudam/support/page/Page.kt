package com.sseudam.support.page

data class Page<T>(
    val content: List<T>,
    val totalCount: Long,
) {
    companion object {
        fun <T> of(
            content: List<T>,
            totalCount: Long,
        ): Page<T> {
            val count =
                if (content.isEmpty()) {
                    0
                } else {
                    totalCount
                }
            return Page(content, count)
        }
    }
}
