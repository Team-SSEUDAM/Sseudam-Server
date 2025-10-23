package com.sseudam.client.notification

import com.sseudam.notification.dto.FirebaseCloudMessage
import com.sseudam.notification.repository.FcmRepository
import org.springframework.stereotype.Repository

@Repository
class FcmClientRepository(
    private val firebaseCloudMessageSender: FirebaseCloudMessageSender,
) : FcmRepository {
    override fun sendAll(firebaseCloudMessages: List<FirebaseCloudMessage>): List<FirebaseCloudMessage> {
        val fcmRequests =
            firebaseCloudMessages.map {
                FcmSendRequest(
                    it.fcmToken,
                    it.title,
                    it.body,
                    it.destination,
                )
            }

        val batchResult = firebaseCloudMessageSender.sendAsync(fcmRequests).get()

        return firebaseCloudMessages.mapIndexed { index, pushMessage ->
            FirebaseCloudMessage(
                fcmKey = pushMessage.fcmKey,
                fcmToken = pushMessage.fcmToken,
                title = pushMessage.title,
                body = pushMessage.body,
                destination = pushMessage.destination,
                tryCount = pushMessage.tryCount + 1,
                sent = batchResult.responses[index].isSuccessful,
            )
        }
    }

    override fun send(firebaseCloudMessage: FirebaseCloudMessage): FirebaseCloudMessage {
        val request =
            FcmSendRequest(
                fcmToken = firebaseCloudMessage.fcmToken,
                title = firebaseCloudMessage.title,
                body = firebaseCloudMessage.body,
                destination = firebaseCloudMessage.destination,
            )
        val sendResult = firebaseCloudMessageSender.sendAsync(request).get()
        return FirebaseCloudMessage(
            fcmKey = firebaseCloudMessage.fcmKey,
            fcmToken = firebaseCloudMessage.fcmToken,
            title = firebaseCloudMessage.title,
            body = firebaseCloudMessage.body,
            destination = firebaseCloudMessage.destination,
            tryCount = firebaseCloudMessage.tryCount + 1,
            sent = sendResult.isNotBlank(),
        )
    }
}
