package com.github.shmvanhouten.adventofcode2019.day15

import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Coordinate
import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Direction
import com.github.shmvanhouten.adventofcode2019.day02.Computer
import com.github.shmvanhouten.adventofcode2019.day02.IntCode

fun calculateTimeNeededToFillUp(intCode: IntCode): Int {
    val (pathToMachine, map) = PathFinder().findPathAndMap(
        RepairDroid(Computer(intCode), Coordinate(0, 0))
    )
    val oxygenMachine = pathToMachine.last()
    val unfilledSpaces = (map - oxygenMachine).toMutableSet()
    var nextNodes = setOf(oxygenMachine)
    val oxygenatedSpaces = nextNodes.toMutableSet()
    var minutes = 0
    while (nextNodes.isNotEmpty()) {
        unfilledSpaces -= nextNodes
        nextNodes = nextNodes.flatMap { node ->
            Direction.values().map { node.move(it) }
        }.filter { unfilledSpaces.contains(it) }
            .toSet()
        oxygenatedSpaces.addAll(nextNodes)
        minutes++
//        println(minutes)
//        println(draw(unfilledSpaces, oxygenatedSpaces))
    }
    return minutes
}

private fun draw(
    coordinates: Collection<Coordinate>,
    oxygenatedSpaces: Collection<Coordinate>
    ): String {
    (coordinates + oxygenatedSpaces)
    return (-21..19).joinToString("\n") { y ->
        (-21..19).joinToString("") { x ->
            when {
                coordinates.contains(Coordinate(x, y)) -> " "
                oxygenatedSpaces.contains(Coordinate(x, y)) -> "o"
                else -> '\u2588'.toString()
            }
        }
    }
}
