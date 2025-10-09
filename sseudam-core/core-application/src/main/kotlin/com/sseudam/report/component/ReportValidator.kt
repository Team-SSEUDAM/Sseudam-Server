package com.sseudam.report.component

import com.sseudam.report.ReportStatus
import com.sseudam.report.SpotReport
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import org.springframework.stereotype.Component

@Component
class ReportValidator {
    fun verifyReport(
        userId: Long,
        report: SpotReport.Info,
    ) {
        if (report.userId != userId) {
            throw ErrorException(ErrorType.UNAUTHORIZED_REPORT)
        }
        if (report.status == ReportStatus.APPROVE) {
            throw ErrorException(ErrorType.ALREADY_APPROVED_REPORT)
        }
    }
}
