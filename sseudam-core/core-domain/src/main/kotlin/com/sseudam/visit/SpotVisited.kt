package com.sseudam.visit

import java.time.LocalDateTime

class SpotVisited {
    /** SpotVisited Create
     * @property userId 방문한 사용자 ID
     * @property spotId 방문한 쓰레기통 장소 ID
     */
    data class Create(
        val userId: Long,
        val spotId: Long,
    )

    /** SpotVisited Info
     * @property id
     * @property userId 방문한 사용자 ID
     * @property spotId 방문한 쓰레기통 장소 ID
     * @property createdAt 방문 시간
     */
    data class Info(
        val id: Long,
        val spotId: Long,
        val userId: Long,
        val createdAt: LocalDateTime,
    )
}
