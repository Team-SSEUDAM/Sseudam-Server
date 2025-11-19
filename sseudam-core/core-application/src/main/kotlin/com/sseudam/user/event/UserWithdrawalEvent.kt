package com.sseudam.user.event

/**
 * 사용자 탈퇴 이벤트
 * Auth 모듈에서 이벤트를 리스닝하여 인증 정보를 삭제합니다.
 */
data class UserWithdrawalEvent(
    val userKey: String,
)
