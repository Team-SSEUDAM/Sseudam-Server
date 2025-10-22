package com.sseudam.application.trashspot

import com.sseudam.DevelopTest
import com.sseudam.trashspot.component.TrashSpotUpdater
import com.sseudam.trashspot.repository.TrashSpotRepository
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

@DevelopTest
class TrashSpotUpdaterTest :
    DescribeSpec({
        val trashSpotRepository: TrashSpotRepository = mockk(relaxed = true)
        val updater = TrashSpotUpdater(trashSpotRepository)

        describe("updateAsEmptySpot") {
            context("쓰레기통이 없는 장소로 신고된 경우") {
                it("해당 쓰레기통을 삭제 처리한다") {
                    val spotId = 1L

                    every { trashSpotRepository.updateAsEmptySpot(spotId) } returns Unit

                    updater.updateAsEmptySpot(spotId)

                    verify { trashSpotRepository.updateAsEmptySpot(spotId) }
                }
            }
        }
    })
