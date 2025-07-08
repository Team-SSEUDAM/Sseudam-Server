package com.sseudam.notification

import com.sseudam.user.device.UserDeviceReader
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class FcmSender(
    private val fcmRepository: FcmRepository,
    private val fcmMessageKeyGenerator: FcmMessageKeyGenerator,
    private val notificationStoredKeyGenerator: NotificationStoredKeyGenerator,
    private val userDeviceReader: UserDeviceReader,
    private val notificationStoredAppender: NotificationStoredAppender,
) {
    @Async
    fun send(
        sendNotificationMessage: SendNotificationMessage,
        type: String,
        parameterValue: String,
    ) {
        val mobileDevices = userDeviceReader.readAllByUserId(sendNotificationMessage.userId)
        val mobileDevice = mobileDevices.last()
        val messages =
            FirebaseCloudMessage(
                fcmKey = fcmMessageKeyGenerator.generateFcmKey(),
                fcmToken = mobileDevice.fcmToken,
                title = sendNotificationMessage.title,
                body = sendNotificationMessage.body,
                tryCount = 0,
                sent = false,
            )
        fcmRepository.send(messages)
        notificationStoredAppender.append(
            NotificationStored.Create(
                notificationStoredKey = notificationStoredKeyGenerator.generate(),
                userId = sendNotificationMessage.userId,
                type = type,
                parameterValue = parameterValue,
                topic = sendNotificationMessage.title,
                contents = sendNotificationMessage.body,
                readStatus = ReadStatus.UNREAD,
            ),
        )
    }

    fun sendAll(
        newMessages: Set<NewFirebaseCloudMessage>,
        maxTry: Int = 3,
    ): List<FirebaseCloudMessage> {
        if (newMessages.isEmpty()) return emptyList()

        val messages =
            newMessages
                .map {
                    FirebaseCloudMessage(
                        fcmKey = fcmMessageKeyGenerator.generateFcmKey(),
                        fcmToken = it.fcmToken,
                        title = it.title,
                        body = it.body,
                        tryCount = 0,
                        sent = false,
                    )
                }.toSet()

        return sendWithRetries(messages, maxTry)
    }

    @Async
    fun sendAllAsync(
        newMessages: Set<NewFirebaseCloudMessage>,
        maxTry: Int = 3,
        callback: (List<FirebaseCloudMessage>) -> Unit = {},
    ) {
        val result = sendAll(newMessages, maxTry)
        callback(result)
    }

    private fun sendWithRetries(
        messages: Set<FirebaseCloudMessage>,
        maxTry: Int,
    ): List<FirebaseCloudMessage> {
        val sent = mutableSetOf<FirebaseCloudMessage>()
        val failed = mutableSetOf<FirebaseCloudMessage>()
        var pending = messages.toList()

        for (attempt in 1..maxTry) {
            if (pending.isEmpty()) break

            val result = fcmRepository.sendAll(pending)

            sent += result.filter { it.sent }
            failed += result.filter { !it.sent && it.tryCount >= maxTry }
            pending = result.filter { !it.sent && it.tryCount < maxTry }
        }

        return buildList {
            addAll(sent)
            addAll(failed)
            addAll(pending)
        }
    }
}
