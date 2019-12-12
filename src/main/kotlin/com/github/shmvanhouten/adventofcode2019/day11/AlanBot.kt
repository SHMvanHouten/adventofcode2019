package com.github.shmvanhouten.adventofcode2019.day11

import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Coordinate
import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Direction.NORTH
import com.github.shmvanhouten.adventofcode2019.day02.ExecutionType
import com.github.shmvanhouten.adventofcode2019.day02.ExecutionType.REQUIRES_INPUT
import com.github.shmvanhouten.adventofcode2019.day02.IComputer

private const val WHITE = 1L
private const val BLACK = 0L

class AlanBot(private val computer: IComputer, val panels:MutableSet<Coordinate> = mutableSetOf()) {
    private var location = Coordinate(0, 0)

    private var orientation = NORTH

    var locationPainted: Coordinate = location

    fun tick(): Boolean {
        val computerState = tellComputerColor()
        return if(computerState == REQUIRES_INPUT) {
            paint()
            turn()
            move()
            true
        } else {
            false
        }

    }

    private fun tellComputerColor(): ExecutionType {
        return if (panels.contains(location)) {
            computer.run(WHITE)
        } else {
            computer.run(BLACK)
        }
    }

    private fun paint() {
        when (computer.output.poll()) {
            WHITE -> panels.add(location)
            BLACK -> panels.remove(location)
            else -> throw IllegalStateException("unknown instruction")
        }
        locationPainted = location
    }

    private fun turn() {
        orientation = when(computer.output.poll()) {
            0L -> orientation.turnLeft()
            1L -> orientation.turnRight()
            else -> throw IllegalStateException("unknown instruction")
        }

    }

    private fun move() {
        location = location.move(orientation)
    }

}