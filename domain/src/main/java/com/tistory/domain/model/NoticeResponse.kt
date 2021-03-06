package com.tistory.domain.model

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
