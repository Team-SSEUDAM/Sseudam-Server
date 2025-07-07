package com.sseudam.notification

object NotificationRegularMessage {
    private val messages: List<Pair<String, String>> =
        listOf(
            "쓰담" to ", 널 기다리고 있어~ \n오늘도 너랑 놀 수 있으면 좋겠다!",
            "쓰담" to "! 오늘 쓰담해주면 \n내가 엄청 귀여운 표정 보여줄게",
            "쓰담" to "! 나 심심해 \n오늘도 너랑 놀 수 있으면 좋겠다!",
            "쓰담" to ", 너랑 있는 시간이 제일 좋아! \n오늘도 나랑 놀자!",
            "쓰담" to ", 우리 오늘은 뭐하고 놀까? \n너를 알게 되어 너무 기뻐!",
        )

    fun randomMessage(): Pair<String, String> = messages.random()
}
