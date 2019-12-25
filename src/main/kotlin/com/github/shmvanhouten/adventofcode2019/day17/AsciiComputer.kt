package com.github.shmvanhouten.adventofcode2019.day17

import com.github.shmvanhouten.adventofcode2019.day02.ExecutionType.RUN
import com.github.shmvanhouten.adventofcode2019.day02.IComputer

class AsciiComputer(val computer: IComputer) {
    fun print(): String {
        var executionType = RUN
        while (executionType == RUN) {
            executionType = computer.run()
        }
        return computer.output.map { it.toChar() }.joinToString("")
    }

    fun run(vararg inputs: String): String {
        for (input in inputs) {
            doInput(input)
        }
        return computer.output.joinToString("") { toAsciiOrInt(it) }
    }

    private fun toAsciiOrInt(it: Long): String {
        return if (it > 128) {
            it.toString()
        } else {
            it.toChar().toString()
        }
    }

    private fun doInput(input: String) {
        input.forEach { c ->
            computer.input(c.toLong())
        }

    }
}