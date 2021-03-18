package com.tistory.blackjinbase.sample.data

import java.util.*

object SampleData {

    fun get(firstItem: String = "Hello"): List<String> {

        val tempList = mutableListOf(firstItem)

        for (i in 0..100) {
            tempList.add("BlackJin$i")
        }

        return tempList
    }

    fun getShuffle(firstItem: String = "Hello Shuffle"): List<String> {

        val tempList = mutableListOf(firstItem)
        val randomCount = Random().nextInt(100)

        (0..randomCount).forEach { _ ->
            val randomNumber = Random().nextInt(100)
            tempList.add("BlackJin$randomNumber")
        }

        return tempList
    }
}