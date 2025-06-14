package com.sseudam.trashspot.image

class TrashSpotImage {
    /** TrashSpotImage Create
     * @property trashSpotId 쓰레기통 id
     * @property imageUrl 이미지 url
     */
    data class Create(
        val trashSpotId: Long,
        val imageUrl: String,
    )

    /** TrashSpotImage Info
     * @property id 이미지 id
     * @property trashSpotId 쓰레기통 id
     * @property imageUrl 이미지 url
     */
    data class Info(
        val id: Long,
        val trashSpotId: Long,
        val imageUrl: String,
    )
}
