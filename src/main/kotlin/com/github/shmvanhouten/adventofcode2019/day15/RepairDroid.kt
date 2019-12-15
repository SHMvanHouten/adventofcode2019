package com.github.shmvanhouten.adventofcode2019.day15

import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Coordinate
import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Direction
import com.github.shmvanhouten.adventofcode2019.day02.IComputer

class RepairDroid(private val computer: IComputer, val location: Coordinate) {

    fun move(direction: Direction): Pair<Int, RepairDroid?> {
        val newComputer = computer.copy()
        newComputer.run(toComputerInstruction(direction))
        val output = computer.output.poll()
        return when(output) {
            0L -> 0 to null
            1L -> 1 to RepairDroid(newComputer, location.move(direction))
            2L -> 2 to RepairDroid(newComputer, location.move(direction))
            else -> throw IllegalStateException("unexpected output: $output")
        }
    }

    private fun toComputerInstruction(direction: Direction): Long {
        return when(direction) {
            Direction.NORTH -> 1L
            Direction.EAST -> 4L
            Direction.SOUTH -> 2L
            Direction.WEST -> 3L
        }
    }
}