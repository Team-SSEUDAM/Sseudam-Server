package com.sseudam.storage.db.core.user

import com.sseudam.support.cursor.OffsetPageRequest
import com.sseudam.user.UserProfile
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository

@Repository
class UserCustomRepository(
    private val userJpaRepository: UserJpaRepository,
) {
    fun readAllBy(offsetPageRequest: OffsetPageRequest): List<UserProfile> {
        val pageable =
            PageRequest.of(
                offsetPageRequest.page,
                offsetPageRequest.size,
                Sort.by(Sort.Direction.DESC, "createdAt"),
            )
        val users =
            userJpaRepository.findPage(pageable) {
                select(entity(UserEntity::class))
                    .from(entity(UserEntity::class))
                    .whereAnd(
                        path(UserEntity::deletedAt).isNull(),
                    )
            }
        return users.map { it!!.toProfile() }.toList()
    }
}
