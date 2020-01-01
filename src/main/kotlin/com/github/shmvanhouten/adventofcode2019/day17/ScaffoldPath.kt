package com.github.shmvanhouten.adventofcode2019.day17

import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Coordinate
import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Direction
import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Direction.NORTH
import com.github.shmvanhouten.adventofcode2019.day17.Turn.*

fun walkPath(scaffold: Set<Coordinate>, startingPoint: Coordinate): List<String> {
    var currentLocation = startingPoint
    var currentOrientation = NORTH
    val steps = mutableListOf<String>()
    while (true) {
        val (orientation, target, turn, stepsAmount) = moveToNextTurning(currentOrientation, scaffold, currentLocation)
            ?: return steps

        currentLocation = target
        currentOrientation = orientation
        steps += "${turn.code},$stepsAmount"
    }
}

private fun moveToNextTurning(
    currentOrientation: Direction,
    scaffold: Set<Coordinate>,
    currentLocation: Coordinate
): Movement? {
    return Turn.values()
        .map { it to currentOrientation.turn(it) }
        .filter { scaffold.contains(currentLocation.move(it.second)) }
        .map { (turn, orientation) -> stepsToNextTurn(currentLocation.move(orientation), orientation, turn, scaffold) }
        .firstOrNull()
}

fun stepsToNextTurn(
    location: Coordinate,
    direction: Direction,
    turn: Turn,
    scaffold: Set<Coordinate>
): Movement {
    val steps = generateSequence(location) { it.move(direction) }
        .takeWhile { scaffold.contains(it) }
        .toList()
    return Movement(direction, steps.last(), turn, steps.size)
}

private fun Direction.turn(turn: Turn): Direction {
    return when (turn) {
        LEFT -> this.turnLeft()
        RIGHT -> this.turnRight()
    }
}

enum class Turn(val code: String) {
    LEFT("L"),
    RIGHT("R")
}

data class Movement(val direction: Direction, val target: Coordinate, val turn: Turn, val steps: Int)
