package com.sseudam.report

enum class ReportType(
    val displayName: String,
) {
    POINT("쓰레기통 위치"),
    KIND("쓰레기통 종류"),
    PHOTO("쓰레기통 사진"),
    NAME("쓰레기통 이름"),
    EMPTY_SPOT("이 위치에 쓰레기통이 없어요"),
    ETC("기타"),
}
