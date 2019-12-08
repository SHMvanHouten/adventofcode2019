package com.github.shmvanhouten.adventofcode2019.day07

import com.github.shmvanhouten.adventofcode2019.day02.Computer
import com.github.shmvanhouten.adventofcode2019.day02.IntCode
import com.github.shmvanhouten.adventofcode2019.util.listPermutations

// part 2
fun maxFeedbackedThrusterSignal(intCode: IntCode): Int {
    return listPermutations((5..9).toList())
        .map { computeFeedbackedThrusterSignal(intCode, it) }
        .max() ?: -1
}

fun computeFeedbackedThrusterSignal(
    intCode: IntCode,
    thrusterSettings: List<Int>
): Int {
    val computers = setUp(thrusterSettings, intCode)
    var runResult = emptyList<Int>()
    var input = 0
    while (runResult.isEmpty()) {
        for (computer in computers) {
            runResult = computer.run(input)
            input = computer.output.last()
        }
    }
    return input
}

// part 1
fun computeMaxThrusterSignal(intCode: IntCode): Int {
    return listPermutations((0..4).toList())
        .map { computeThrusterSignal(intCode, it) }
        .max() ?: -1
}

fun computeThrusterSignal(intCode: IntCode, thrusterSettings: List<Int>): Int {
    val computers = setUp(thrusterSettings, intCode)

    var input = 0
    for (computer in computers) {
        computer.run(input)
        input = computer.output.last()
    }
    return input
}

private fun setUp(
    thrusterSettings: List<Int>,
    intCode: IntCode
): List<Computer> {
    return thrusterSettings.map { thrusterSetting ->
        val computer = Computer(intCode)
        computer.run(thrusterSetting)
        computer
    }
}

