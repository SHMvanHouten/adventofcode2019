package com.github.shmvanhouten.adventofcode2019.day02

fun findTargetOutput(intCode: IntCode, target: Long): Pair<Long, Long> {
    for (i in 0L..Int.MAX_VALUE) {
        for (j in 0..i) {
            val result: Long = try {
                Computer(intCode = intCode.withInitialParameters(i, j)).run()[0]
            } catch (e: Exception) {
                -1
            }

            if (result == target) {
                return i to j
            }
        }
    }
    throw IllegalStateException("Not found")
}