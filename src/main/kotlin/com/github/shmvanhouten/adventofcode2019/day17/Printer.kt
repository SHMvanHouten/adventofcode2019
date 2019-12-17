package com.github.shmvanhouten.adventofcode2019.day17

import com.github.shmvanhouten.adventofcode2019.day02.ExecutionType.RUN
import com.github.shmvanhouten.adventofcode2019.day02.IComputer

class Printer(val computer: IComputer) {
    fun print(): String {
        var executionType = RUN
        while (executionType == RUN) {
            executionType = computer.run()
        }
        return computer.output.map { it.toChar() }.joinToString("")
    }
}