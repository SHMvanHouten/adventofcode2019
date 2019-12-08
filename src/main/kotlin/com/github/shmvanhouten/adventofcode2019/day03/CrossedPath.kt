package com.github.shmvanhouten.adventofcode2019.day03

import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Coordinate
import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Direction
import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Direction.*
import com.github.shmvanhouten.adventofcode2019.util.splitIntoTwo
import kotlin.math.abs

fun findDistanceFromNearestCrossing(wire1: Wire, wire2: Wire): Int {
    return findNearestCrossing(wire1, wire2)?.distanceFromOrigin() ?: -1
}

fun findNearestCrossing(wire1: Wire, wire2: Wire): Coordinate? {
    val wire1Route = layoutWire(wire1)
    val wire2Route = layoutWire(wire2)
    return wire1Route
        .filter { it != Coordinate(0, 0) }
        .filter { wire2Route.contains(it) }
        .minBy { abs(it.x) + abs(it.y) }
}

fun findLeastStepsToCrossing(wire1: Wire, wire2: Wire): Int {
    val wire1Coords = layoutWire(wire1)
    val wire2Coords = layoutWire(wire2)
    val wire1Route: List<Coordinate> = traceWire(wire1)
    val wire2Route: List<Coordinate> = traceWire(wire2)
    return wire1Coords
        .filter { it != Coordinate(0, 0) }
        .filter { wire2Coords.contains(it) }
        .map { countSteps(it, wire1Route, wire2Route) }
        .min()!!
}

fun countSteps(crossing: Coordinate, wire1: List<Coordinate>, wire2Route: List<Coordinate>): Int {
    return wire1.takeWhile { it != crossing }.count() + wire2Route.takeWhile { it != crossing }.count()
}

fun traceWire(wire: Wire): List<Coordinate> {
    var currentLocation = Coordinate(0, 0)
    val allCoordinates = mutableListOf(currentLocation)
    for (stretch in wire.stretches) {
        val (coordinates, newLocation) = layoutStretch(stretch, currentLocation)
        currentLocation = newLocation
        allCoordinates += coordinates
    }
    return allCoordinates
}

fun layoutWire(wire: Wire): LinkedHashSet<Coordinate> {
    var currentLocation = Coordinate(0, 0)
    val allCoordinates = LinkedHashSet<Coordinate>()
    allCoordinates.add(currentLocation)
    for (stretch in wire.stretches) {
        val (coordinates, newLocation) = layoutStretch(stretch, currentLocation)
        currentLocation = newLocation
        allCoordinates += coordinates
    }
    return allCoordinates
}

fun layoutStretch(
    stretch: Stretch,
    startingLocation: Coordinate
): Pair<List<Coordinate>, Coordinate> {
    return when (stretch.direction) {
        NORTH -> goUp(startingLocation, stretch.distance)
        EAST -> goRight(startingLocation, stretch.distance)
        SOUTH -> goDown(startingLocation, stretch.distance)
        WEST -> goLeft(startingLocation, stretch.distance)
    }
}

fun goUp(location: Coordinate, distance: Int): Pair<List<Coordinate>, Coordinate> {
    val coordinates = (location.y + 1..location.y + distance).map { Coordinate(location.x, it) }
    return coordinates to coordinates.last()
}

fun goDown(location: Coordinate, distance: Int): Pair<List<Coordinate>, Coordinate> {
    val coordinates = (location.y - distance).until(location.y).reversed().map { Coordinate(location.x, it) }
    return coordinates to coordinates.last()
}

fun goRight(location: Coordinate, distance: Int): Pair<List<Coordinate>, Coordinate> {
    val coordinates = (location.x + 1..location.x + distance).map { Coordinate(it, location.y) }
    return coordinates to coordinates.last()
}

fun goLeft(location: Coordinate, distance: Int): Pair<List<Coordinate>, Coordinate> {
    val coordinates = (location.x - distance).until(location.x).reversed().map { Coordinate(it, location.y) }
    return coordinates to coordinates.last()
}

private fun Coordinate.distanceFromOrigin(): Int {
    return abs(this.x) + abs(this.y)
}

class Wire(val stretches: List<Stretch>) {

    constructor(input: String) : this(input.split(',').map { toStep(it) })

}

data class Stretch(val direction: Direction, val distance: Int)

private fun toStep(rawStep: String): Stretch {
    val (direction, steps) = rawStep.splitIntoTwo(1)
    return Stretch(toDirection(direction), steps.toInt())
}

fun toDirection(raw: String): Direction {
    return when (raw) {
        "U" -> NORTH

        "R" -> EAST

        "D" -> SOUTH

        "L" -> WEST
        else -> throw IllegalStateException("Unknown direction $raw")
    }
}
