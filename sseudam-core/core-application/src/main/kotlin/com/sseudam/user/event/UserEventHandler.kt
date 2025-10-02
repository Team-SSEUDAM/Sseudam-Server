package com.sseudam.user.event

import com.sseudam.notification.discord.DiscordClient
import com.sseudam.pet.Pet
import com.sseudam.pet.component.PetReader
import com.sseudam.pet.component.UserPetAppender
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import com.sseudam.user.component.UserReader
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class UserEventHandler(
    private val petReader: PetReader,
    private val userPetAppender: UserPetAppender,
    private val userReader: UserReader,
    private val discordClient: DiscordClient,
) {
    @ApplicationModuleListener(id = "user-signup-create-pet", condition = "#event.userId != null")
    fun onCreatePetListener(event: UserSignUpEvent) {
        val (currentYear, currentMonth) = LocalDate.now().let { it.year to it.month }

        val pets = petReader.readAllLatestSeasonPets(currentYear, currentMonth)
        val level1Pet =
            pets.find { it.levelType == Pet.LevelType.LEVEL_1 }
                ?: throw ErrorException(ErrorType.INVALID_PET_LEVEL_TYPE)
        userPetAppender.append(event.userId, level1Pet)
    }

    @ApplicationModuleListener(id = "user-signup-discord-notification", condition = "#event.userId != null")
    fun onDiscordNotificationListener(event: UserSignUpEvent) {
        val userProfile =
            userReader.readUserProfile(event.userId)
                ?: throw ErrorException(ErrorType.NOT_FOUND_USER)
        discordClient.sendCreateUserMessage(userProfile)
    }
}
