package com.tistory.blackjinbase.liverecyclerview

interface DiffComparable<T> {
    fun areItemsTheSame(item: T): Boolean
    fun areContentsTheSame(item: T): Boolean
}