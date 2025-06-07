package com.sseudam.storage.db.core.trashspot.image

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.springframework.data.jpa.repository.JpaRepository

interface TrashSpotImageJpaRepository :
    JpaRepository<TrashSpotImageEntity, Long>,
    KotlinJdslJpqlExecutor {
    fun findAllByTrashSpotIdIn(map: List<Long>): List<TrashSpotImageEntity>

    fun findAllByTrashSpotId(spotId: Long): List<TrashSpotImageEntity>
}
