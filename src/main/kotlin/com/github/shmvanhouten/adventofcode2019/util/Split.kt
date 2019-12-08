package com.github.shmvanhouten.adventofcode2019.util

fun String.splitIntoTwo(index: Int): Pair<String, String> {
    val firstPart = this.substring(0, index)
    val secondPart = this.substring(index, this.length)
    return Pair(firstPart, secondPart)
}