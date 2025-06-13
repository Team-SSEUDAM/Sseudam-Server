package com.sseudam.suggestion

import com.sseudam.suggestion.event.SuggestionUpdateEvent
import com.sseudam.trashspot.TrashSpotService
import com.sseudam.trashspot.image.TrashSpotImage
import com.sseudam.trashspot.image.TrashSpotImageService
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class SuggestionEventListener(
    private val trashSpotService: TrashSpotService,
    private val trashSpotImageService: TrashSpotImageService,
) {
    @Async
    @EventListener
    fun createSuggestionListener(event: SuggestionUpdateEvent) {
        val trashSpot = trashSpotService.createTrashSpotBySuggestion(event.suggestion)
        trashSpotImageService.append(
            TrashSpotImage.Create(
                trashSpot.id,
                event.suggestion.imageUrl,
            ),
        )
    }

    // TODO: Update Suggestion Notification
}
