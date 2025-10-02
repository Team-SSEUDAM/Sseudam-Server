package com.sseudam.report.event

import com.sseudam.pet.PetPointAction
import com.sseudam.report.SpotReport

data class SpotReportCreatedEvent(
    val spotReport: SpotReport.Info,
    val userId: Long,
    val petPointAction: PetPointAction,
)
