package com.github.shmvanhouten.adventofcode2019.day02

fun findTargetOutput(intCode: IntCode, target: Int): Pair<Int, Int> {
    for (i in 0..Int.MAX_VALUE) {
        for (j in 0..i) {
            val result: Int = try {
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