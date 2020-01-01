package com.github.shmvanhouten.adventofcode2019.day17

import com.github.shmvanhouten.adventofcode2019.day02.Computer
import com.github.shmvanhouten.adventofcode2019.day02.IntCode

class ScaffoldBot(intCode: List<Long>) {
    private val asciiComputer: AsciiComputer

    init {
        asciiComputer = initiateComputer(intCode)
    }

    fun reportOnScaffold(): Int {
        val scaffold = asciiComputer.print()
        val path = walkPath(parseScaffold(scaffold), findStartingPoint(scaffold))
        println("Path is $path")
        println("For scaffold:\n$scaffold")
        val movementRoutine = compressPathsToRoutines(path)

        input(asciiComputer, movementRoutine.main)
        input(asciiComputer, movementRoutine.a)
        input(asciiComputer, movementRoutine.b)
        input(asciiComputer, movementRoutine.c)

        val result = input(asciiComputer, "n\n")
        return result.split('\n').last().toInt()
    }

    private fun initiateComputer(intCode: List<Long>): AsciiComputer {
        val computer = Computer(IntCode(intCode))
        computer.run()
        return AsciiComputer(computer)
    }

    private fun input(
        asciiComputer: AsciiComputer,
        input: String
    ): String {
        println(input)
        val result = asciiComputer.run(input)
        println(result)
        asciiComputer.clear()
        return result
    }
}