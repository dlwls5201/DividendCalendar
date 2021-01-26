package com.tistory.dividendcalendar.presentation.notice.model

data class NoticeItem(
    val id: Int = -1,
    val title: String = "",
    val description: String = "",
    val updateDate: String = "",
    var expandable: Boolean = false
)