package com.sseudam.storage.db.core.report

import com.sseudam.report.SpotReportRepository
import org.springframework.stereotype.Repository

@Repository
class SpotReportCoreRepository(
    private val spotReportJpaRepository: SpotReportJpaRepository,
) : SpotReportRepository
