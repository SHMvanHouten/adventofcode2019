package com.github.shmvanhouten.adventofcode2019.day15

import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Coordinate
import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Direction
import com.github.shmvanhouten.adventofcode2019.day02.Computer
import com.github.shmvanhouten.adventofcode2019.day02.IntCode

fun calculateTimeNeededToFillUp(intCode: IntCode): Int {
    val (map, oxygenMachine) = exploreMapAndFindOxygenMachine(intCode)

    val unfilledSpaces = (map - oxygenMachine).toMutableSet()
    var nextNodes = setOf(oxygenMachine)
    val oxygenatedSpaces = nextNodes.toMutableSet()
    var minutes = 0
    while (unfilledSpaces.isNotEmpty()) {
        nextNodes = findAdjoiningUnoxyginatedSpaces(nextNodes, unfilledSpaces)
        oxygenatedSpaces.addAll(nextNodes)
        unfilledSpaces -= nextNodes
        minutes++
//        println(draw(unfilledSpaces, oxygenatedSpaces))
    }
    return minutes
}

private fun exploreMapAndFindOxygenMachine(intCode: IntCode): Pair<MutableSet<Coordinate>, Coordinate> {
    val (pathToMachine, map) = PathFinder().findPathAndMap(
        RepairDroid(Computer(intCode), Coordinate(0, 0))
    )
    return map to pathToMachine.last()
}

private fun findAdjoiningUnoxyginatedSpaces(
    nextNodes: Set<Coordinate>,
    unfilledSpaces: MutableSet<Coordinate>
): Set<Coordinate> {
    var nextNodes1 = nextNodes
    nextNodes1 = nextNodes1
        .flatMap { node -> moveInAllDirections(node) }
        .filter { unfilledSpaces.contains(it) }
        .toSet()
    return nextNodes1
}

private fun moveInAllDirections(node: Coordinate) =
    Direction.values().map { node.move(it) }

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
