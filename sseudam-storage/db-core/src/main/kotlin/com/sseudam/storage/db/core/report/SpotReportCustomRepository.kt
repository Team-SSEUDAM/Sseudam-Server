package com.sseudam.storage.db.core.report

import com.sseudam.report.ReportType
import com.sseudam.report.SpotReport
import com.sseudam.storage.db.core.support.JDSLExtensions
import com.sseudam.support.cursor.OffsetPageRequest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository

@Repository
class SpotReportCustomRepository(
    private val spotReportJpaRepository: SpotReportJpaRepository,
) {
    fun findAllBy(
        offsetPageRequest: OffsetPageRequest,
        searchType: ReportType?,
    ): List<SpotReport.Info> {
        val pageable =
            PageRequest.of(
                offsetPageRequest.page,
                offsetPageRequest.size,
                Sort.by(Sort.Direction.DESC, "createdAt"),
            )
        val suggestions =
            spotReportJpaRepository.findPage(JDSLExtensions, pageable) {
                select(entity(SpotReportEntity::class))
                    .from(entity(SpotReportEntity::class))
                    .whereAnd(
                        path(SpotReportEntity::deletedAt).isNull(),
                        searchType?.let {
                            path(SpotReportEntity::reportType).eq(it)
                        },
                    )
            }
        return suggestions
            .filterNotNull()
            .map { it.toSpotReport() }
    }
}
