package com.sseudam.notification

interface FcmRepository {
    fun sendAll(firebaseCloudMessages: List<FirebaseCloudMessage>): List<FirebaseCloudMessage>

    fun send(firebaseCloudMessage: FirebaseCloudMessage): FirebaseCloudMessage
}
