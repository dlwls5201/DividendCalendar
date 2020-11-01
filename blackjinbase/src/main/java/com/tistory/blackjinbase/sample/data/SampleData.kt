package com.tistory.blackjinbase.sample.data

object SampleData {

    fun get(firstItem: String = "Hello"): List<String> {

        val tempList = mutableListOf(firstItem)

        for (i in 0..100) {
            tempList.add("BlackJin$i")
        }

        return tempList
    }
}