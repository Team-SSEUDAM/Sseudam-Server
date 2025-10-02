package com.sseudam.notification.repository

import com.sseudam.notification.dto.FirebaseCloudMessage

interface FcmRepository {
    fun sendAll(firebaseCloudMessages: List<FirebaseCloudMessage>): List<FirebaseCloudMessage>

    fun send(firebaseCloudMessage: FirebaseCloudMessage): FirebaseCloudMessage
}
