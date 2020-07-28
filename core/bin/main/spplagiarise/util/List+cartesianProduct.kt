package spplagiarise.util

import java.util.*

fun <T> List<List<T>>.cartesianProduct(): List<List<T>> {
    val resultLists = ArrayList<List<T>>()

    val min = this.map { it.size }.min() ?: 0

    for (i in 0 until min) {
        val currentRound = mutableListOf<T>()
        for (list in this) {
            currentRound.add(list[i])
        }
        resultLists.add(currentRound)
    }

    return resultLists

    if (this.isEmpty()) {
        resultLists.add(ArrayList())
        return resultLists
    } else {
        val firstList = this[0]
        val remainingLists = this.subList(1, this.size).cartesianProduct()
        for (condition in firstList) {
            for (remainingList in remainingLists) {
                val resultList = ArrayList<T>()
                resultList.add(condition)
                resultList.addAll(remainingList)
                resultLists.add(resultList)
            }
        }
    }
    return resultLists
}