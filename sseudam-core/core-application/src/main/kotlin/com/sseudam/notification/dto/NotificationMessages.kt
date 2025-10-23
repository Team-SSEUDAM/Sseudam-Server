package com.sseudam.notification.dto

import com.sseudam.pet.PetPointAction

object NotificationMessages {
    private val regularMessages: List<Pair<String, String>> =
        listOf(
            "쓰담" to ", 널 기다리고 있어~ \n오늘도 너랑 놀 수 있으면 좋겠다!",
            "쓰담" to "! 오늘 쓰담해주면 \n내가 엄청 귀여운 표정 보여줄게",
            "쓰담" to "! 나 심심해 \n오늘도 너랑 놀 수 있으면 좋겠다!",
            "쓰담" to ", 너랑 있는 시간이 제일 좋아! \n오늘도 나랑 놀자!",
            "쓰담" to ", 우리 오늘은 뭐하고 놀까? \n너를 알게 되어 너무 기뻐!",
        )

    const val DEFAULT_TITLE = "쓰담"
    const val APPROVE_SUGGESTION_CONTENTS = "님이 제보한 쓰레기통이 승인되었어요! \n지금 바로 확인하러 가볼까요?"
    const val REJECT_SUGGESTION_CONTENTS = "님이 제보한 쓰레기통이 거절되었어요! \n다음에는 더 좋은 제보 부탁드려요!"

    const val APPROVE_REPORT_CONTENTS = "님이 신고한 쓰레기통이 승인되었어요! \n지금 바로 확인하러 가볼까요?"
    const val REJECT_REPORT_CONTENTS = "님이 신고한 쓰레기통이 거절되었어요! \n다음에는 더 좋은 제보 부탁드려요!"

    fun newPetContents(nickname: String) = "새로운 고양이가 ${nickname}님 곁을 찾아왔어요! \n지금 만나러 가볼까요?"

    fun anonymousVisitedSpotContents(nickname: String) =
        "${nickname}님이 제보한 \n" +
            "쓰레기통에 쓰레기가 버려졌어요."

    fun randomRegularMessage(): Pair<String, String> = regularMessages.random()

    fun approveSuggestionContents(nickname: String) =
        "${nickname}님이 제보한 쓰레기통이 승인되어 ${PetPointAction.SUGGESTION_APPROVED.point}포인트를 추가로 받았어요!"

    fun rejectSuggestionContents(nickname: String) = "${nickname}님이 제보한 쓰레기통이 반려되었어요. 사유를 확인해볼까요?"

    fun approveReportContents(nickname: String) =
        "${nickname}님이 수정 제안한 쓰레기통이 승인되어 ${PetPointAction.SUGGESTION_APPROVED.point}포인트를 추가로 받았어요!"

    fun rejectReportContents(nickname: String) = "${nickname}님이 수정 제안한 쓰레기통이 반려되었어요. 사유를 확인해볼까요?"
}
