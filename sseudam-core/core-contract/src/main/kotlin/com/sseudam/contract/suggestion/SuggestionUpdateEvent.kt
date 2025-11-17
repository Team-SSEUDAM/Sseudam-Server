package com.sseudam.contract.suggestion

/**
 * Suggestion 업데이트 이벤트
 *
 * 순환 의존성을 방지하기 위해 contract 모듈에 정의되어 있습니다.
 */
data class SuggestionUpdateEvent(
    val suggestion: SuggestionDto,
    val reason: String?,
    val rewardPoint: Long = 0L,
)
