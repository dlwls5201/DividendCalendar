package com.tistory.dividendcalendar.presentation.notice.model

import com.tistory.domain.model.NoticeResponse

data class NoticeItem(
    val id: Int = -1,
    val title: String = "",
    val description: String = "",
    val updateDate: String = "",
    var expandable: Boolean = false
)

fun NoticeResponse.ItemResponse.mapToItem() = NoticeItem(
    id = id,
    title = title,
    description = description,
    updateDate = date
)