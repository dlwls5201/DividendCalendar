package com.tistory.dividendcalendar.presentation.notice.model

data class NoticeResponse(
    val country: String = "",
    val items: List<ItemResponse> = emptyList()
) {
    data class ItemResponse(
        val id: Int = -1,
        val title: String = "",
        val description: String = "",
        val date: String = ""
    )
}

fun NoticeResponse.ItemResponse.mapToItem() = NoticeItem(
    id = id,
    title = title,
    description = description,
    updateDate = date
)
