package com.github.shmvanhouten.adventofcode2019.day16

import kotlin.math.abs

private const val ZERO_ASCII = '0'.toInt()

fun cleanUpSignal(dirtySignal: String, phases: Int): String {
    return 0.until(phases)
        .fold(dirtySignal.map { toInt(it) }) {
                signal, _ -> cleanupPhase(signal, dirtySignal.length)
        }
        .joinToString("").substring(0, 8)

}

private fun cleanupPhase(dirtySignal: List<Int>, length: Int): List<Int> {
    return (1..length).map { i ->
        val pattern: List<Int> = 0.repeat(i) + 1.repeat(i) + 0.repeat(i) + (-1).repeat(i)
        val result = generateSequence { pattern }.flatten().drop(1)
            .asIterable()
            .zip(dirtySignal)
            .sumBy { (p, s) -> p * s }
        abs(result % 10)
    }
}

private fun Int.repeat(i: Int): List<Int> {
    return 0.until(i).map { this }
}

fun toInt(it: Char): Int {
    return it.toInt() - ZERO_ASCII
}
