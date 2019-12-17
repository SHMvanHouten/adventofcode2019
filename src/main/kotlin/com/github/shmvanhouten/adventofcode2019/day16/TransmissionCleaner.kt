package com.github.shmvanhouten.adventofcode2019.day16

import kotlin.math.abs

private const val ZERO_ASCII = '0'.toInt()

fun cleanUpSignal(dirtySignal: String, phases: Int): String {
    return 0.until(phases)
        .fold(dirtySignal.map { toInt(it) }) {
                signal, _ -> cleanupPhase(signal, dirtySignal.length)
        }
        .joinToString("")
}

private fun cleanupPhase(dirtySignal: List<Int>, length: Int): List<Int> {
    return (1..length).map { i ->
        val pattern: List<Int> = listOf(0, 1, 0, -1)
        val result = generateSequence { pattern }.flatten()
            .asIterable()
            .zip(chunkedWithLessChunkForFirst(dirtySignal, i))
            .filter { it.first != 0 }
            .sumBy { (p, s) -> p * s.sum() }
        abs(result % 10)
    }
}

private fun chunkedWithLessChunkForFirst(
    dirtySignal: List<Int>,
    i: Int
): List<List<Int>> {
    val take = dirtySignal.take(i - 1)
    val drop = dirtySignal.drop(i - 1)
    return listOf(take) + drop.chunked(i)
}

private fun toInt(it: Char): Int {
    return it.toInt() - ZERO_ASCII
}

// PART 2

fun quickFind(input: String): String {
    val offset = input.substring(0,7).toInt()
    var result = input.substring(offset).toCharArray().map { toInt(it) }.reversed()
    repeat(100) {
        result = doTheBackwardPattern(result)
    }

    return result.joinToString("").reversed().substring(0, 8)
}

private fun doTheBackwardPattern(input: List<Int>): List<Int> {
    var accumulator = 0
    return input.map {
        accumulator += it
        accumulator %= 10
        accumulator
    }
}
