package com.github.shmvanhouten.adventofcode2019.day07

import com.github.shmvanhouten.adventofcode2019.day02.Computer
import com.github.shmvanhouten.adventofcode2019.day02.ExecutionType.RUN
import com.github.shmvanhouten.adventofcode2019.day02.ExecutionType.STOP
import com.github.shmvanhouten.adventofcode2019.day02.IntCode
import com.github.shmvanhouten.adventofcode2019.util.listPermutations

// part 2
fun maxFeedbackedThrusterSignal(intCode: IntCode): Long {
    return listPermutations((5..9).toList())
        .map { computeFeedbackedThrusterSignal(intCode, it.map{int -> int.toLong()}) }
        .max() ?: -1
}

fun computeFeedbackedThrusterSignal(
    intCode: IntCode,
    thrusterSettings: List<Long>
): Long {
    val computers = setUp(thrusterSettings, intCode)
    var runResult = RUN
    var input = 0L
    while (runResult != STOP) {
        for (computer in computers) {
            runResult = computer.input(input)
            input = computer.output.last()
        }
    }
    return input
}

// part 1
fun computeMaxThrusterSignal(intCode: IntCode): Long {
    return listPermutations((0..4).toList())
        .map { computeThrusterSignal(intCode, it.map{int -> int.toLong()}) }
        .max() ?: -1
}

fun computeThrusterSignal(intCode: IntCode, thrusterSettings: List<Long>): Long {
    val computers = setUp(thrusterSettings, intCode)

    var input = 0L
    for (computer in computers) {
        computer.input(input)
        input = computer.output.last()
    }
    return input
}

private fun setUp(
    thrusterSettings: List<Long>,
    intCode: IntCode
): List<Computer> {
    return thrusterSettings.map { thrusterSetting ->
        val computer = Computer(intCode)
        computer.input(thrusterSetting)
        computer
    }
}

