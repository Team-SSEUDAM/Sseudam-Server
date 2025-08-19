package com.sseudam.storage.db.core.report

import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.render.RenderContext
import com.linecorp.kotlinjdsl.support.spring.data.jpa.extension.createQuery
import com.sseudam.report.ReportType
import com.sseudam.report.SpotReport
import com.sseudam.storage.db.core.report.reject.ReportRejectEntity
import com.sseudam.storage.db.core.support.JDSLExtensions
import com.sseudam.support.cursor.OffsetPageRequest
import com.sseudam.support.page.Page
import jakarta.persistence.EntityManager
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository

@Repository
class SpotReportCustomRepository(
    private val spotReportJpaRepository: SpotReportJpaRepository,
    private val entityManager: EntityManager,
    private val jdslRenderContext: RenderContext,
) {
    fun findAllBy(
        offsetPageRequest: OffsetPageRequest,
        searchType: ReportType?,
    ): Page<SpotReport.Detail> {
        val pageable =
            PageRequest.of(
                offsetPageRequest.page,
                offsetPageRequest.size,
                Sort.by(Sort.Direction.DESC, "createdAt"),
            )
        val reports =
            spotReportJpaRepository.findPage(JDSLExtensions, pageable) {
                selectNew<SpotReport.Detail>(
                    path(SpotReportEntity::id),
                    path(SpotReportEntity::spotId),
                    path(SpotReportEntity::userId),
                    path(SpotReportEntity::reportType),
                    path(SpotReportEntity::point),
                    path(SpotReportEntity::spotName),
                    path(SpotReportEntity::region),
                    path(SpotReportEntity::address),
                    path(SpotReportEntity::trashType),
                    path(SpotReportEntity::imageUrl),
                    path(SpotReportEntity::status),
                    coalesce(path(ReportRejectEntity::reason), null),
                    path(SpotReportEntity::createdAt),
                ).from(
                    entity(SpotReportEntity::class),
                    leftJoin(ReportRejectEntity::class).on(
                        path(SpotReportEntity::id)
                            .eq(path(ReportRejectEntity::reportId)),
                    ),
                ).whereAnd(
//                        path(SpotReportEntity::deletedAt).isNull(),
                    searchType?.let {
                        path(SpotReportEntity::reportType).eq(it)
                    },
                )
            }
        return Page.of(
            content = reports.content.mapNotNull { it },
            totalCount = reports.totalElements,
        )
    }

    fun findAllDetailsByUserId(userId: Long): List<SpotReport.Detail> {
        val query =
            jpql(JDSLExtensions) {
                selectNew<SpotReport.Detail>(
                    path(SpotReportEntity::id),
                    path(SpotReportEntity::spotId),
                    path(SpotReportEntity::userId),
                    path(SpotReportEntity::reportType),
                    path(SpotReportEntity::point),
                    path(SpotReportEntity::spotName),
                    path(SpotReportEntity::region),
                    path(SpotReportEntity::address),
                    path(SpotReportEntity::trashType),
                    path(SpotReportEntity::imageUrl),
                    path(SpotReportEntity::status),
                    coalesce(path(ReportRejectEntity::reason), null),
                    path(SpotReportEntity::createdAt),
                ).from(
                    entity(SpotReportEntity::class),
                    leftJoin(ReportRejectEntity::class).on(
                        path(SpotReportEntity::id)
                            .eq(path(ReportRejectEntity::reportId)),
                    ),
                ).where(
                    path(SpotReportEntity::userId).eq(userId),
                )
            }
        return entityManager.createQuery(query, jdslRenderContext).resultList
    }
}
