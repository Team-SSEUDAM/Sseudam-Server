package com.sseudam.pet.event

/**
 * 새로운 펫 시즌이 생성되었을 때 발행되는 이벤트
 */
data class NewPetSeasonCreatedEvent(
    val year: Int,
    val month: Int,
)
