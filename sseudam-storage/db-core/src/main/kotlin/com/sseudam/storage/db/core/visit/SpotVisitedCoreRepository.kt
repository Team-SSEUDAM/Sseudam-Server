package com.sseudam.storage.db.core.visit

import com.sseudam.visit.SpotVisitedRepository

class SpotVisitedCoreRepository(
    private val spotVisitedJpaRepository: SpotVisitedJpaRepository,
) : SpotVisitedRepository
