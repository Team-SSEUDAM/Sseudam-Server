package com.sseudam.pet

import org.springframework.stereotype.Component

@Component
class PetPointHistoryAppender(
    private val petPointHistoryRepository: PetPointHistoryRepository,
)
