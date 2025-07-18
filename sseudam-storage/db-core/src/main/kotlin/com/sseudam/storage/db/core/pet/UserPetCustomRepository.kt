package com.sseudam.storage.db.core.pet

import com.linecorp.kotlinjdsl.dsl.jpql.Jpql
import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.querymodel.Queryable
import com.linecorp.kotlinjdsl.querymodel.jpql.update.UpdateQuery
import com.linecorp.kotlinjdsl.render.RenderContext
import com.linecorp.kotlinjdsl.support.spring.data.jpa.extension.createQuery
import com.sseudam.storage.db.core.support.JDSLExtensions
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository

@Repository
class UserPetCustomRepository(
    private val entityManager: EntityManager,
    private val jdslRenderContext: RenderContext,
) {
    fun <T : Any> updateAll(update: Jpql.() -> Queryable<UpdateQuery<T>>): Int {
        val query = jpql(JDSLExtensions) { update().toQuery() }
        return entityManager.createQuery(query, jdslRenderContext).executeUpdate()
    }

    fun resetSeasonUserPetPoint(petId: Long): Int =
        updateAll {
            update(entity(UserPetEntity::class))
                .set(path(UserPetEntity::point), 0)
                .set(path(UserPetEntity::petId), petId)
                .where(path(UserPetEntity::deletedAt).isNull())
        }
}
