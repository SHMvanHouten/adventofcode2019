package com.github.shmvanhouten.adventofcode2019.day18

fun breakUpFourMaps(input: String): List<KeysToVectorBetweenKeys> {
    return breakUpIntoParts(input)
        .map { findPathBetweenEachPoint(it.joinToString("\n")) }
}

fun breakUpIntoParts(input: String): List<List<String>> {
    val rows = input.split('\n')
    val (top, bottom) = rows.splitRetainingMiddle((rows.size + 1) / 2)
    val (topLeft, topRight) = splitVertically(top)
    val (bottomLeft, bottomRight) = splitVertically(bottom)

    return listOf(topLeft, topRight, bottomLeft, bottomRight)
}

private fun splitVertically(top: List<String>): Pair<List<String>, List<String>> {
    val split = top.map { it.splitRetainingMiddle((it.length) / 2) }
    return split.map { it.first } to split.map { it.second }
}

private fun <E> List<E>.splitRetainingMiddle(delimiter: Int): Pair<List<E>, List<E>> {
    val firstPart = this.subList(0, delimiter)
    val secondPart = this.subList(delimiter - 1, this.size)
    return Pair(firstPart, secondPart)
}

private fun String.splitRetainingMiddle(delimeter: Int): Pair<String, String> {
    val firstPart = this.substring(0, delimeter + 1)
    val secondPart = this.substring(delimeter)
    return Pair(firstPart, secondPart)
}

typealias KeysToVectorBetweenKeys = Pair<Set<Char>, MutableMap<Char, Map<Char, Vector>>>