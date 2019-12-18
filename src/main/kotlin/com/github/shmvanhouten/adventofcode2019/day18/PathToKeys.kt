package com.github.shmvanhouten.adventofcode2019.day18

import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Coordinate
import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Direction
import java.util.Comparator

fun stepsRequired(input: String): Int {
    val (availableKeys, map) = toMazeState(input)

    val visitedCoordinatesWithKeyAmounts = mutableMapOf<Coordinate, Set<Set<Char>>>()
    val unfinishedRoutes = mutableListOf(Route(listOf(findStartingPoint(map)), emptySet()))

    while (unfinishedRoutes.isNotEmpty()) {
        unfinishedRoutes.sortWith(RouteComparator())
        val (path, keys) = unfinishedRoutes.removeAt(0)
        if(keys == availableKeys) {
            println("Path found!")
            println(path)
            return path.size -1
        }

        val newDirections = moveInValidDirections(path, map, keys)
            .map { toRoute(it, path, keys, map[it]!!) }
            .filter { hasNotBeenHereBeforeWithTheSameKeys(visitedCoordinatesWithKeyAmounts, it)}
        newDirections.forEach { route ->
            visitedCoordinatesWithKeyAmounts.merge(route.path.last(), setOf(route.keys)) { keys1, keys2 -> keys1 + keys2 }
        }
        unfinishedRoutes += newDirections
    }

    throw IllegalStateException("not all keys were found!")
}

private fun hasNotBeenHereBeforeWithTheSameKeys(
    visitedCoordinatesWithKeyAmounts: Map<Coordinate, Set<Set<Char>>>,
    route: Route
): Boolean {
    val lastLocation = route.path.last()
    return !visitedCoordinatesWithKeyAmounts.contains(lastLocation) ||
            !visitedCoordinatesWithKeyAmounts[lastLocation]!!.contains(route.keys)
}

fun toRoute(
    coordinate: Coordinate,
    path: List<Coordinate>,
    keys: Set<Char>,
    c: Char
): Route {
    val newKeys = if(c.isLowerCase() && !keys.contains(c)) {
        keys + c
    } else {
        keys
    }
    return Route(path + coordinate, newKeys)
}

private fun moveInValidDirections(
    path: List<Coordinate>,
    map: Map<Coordinate, Char>,
    keys: Set<Char>
): List<Coordinate> {
    return Direction.values()
        .map { path.last().move(it) }
        .filter { isValidStep(it, map, keys) }
}

fun isValidStep(
    coordinate: Coordinate,
    map: Map<Coordinate, Char>,
    keys: Set<Char>
): Boolean {
    val c = map[coordinate] ?: throw IllegalStateException("nothing found at coordinate $coordinate")
    return c != '#' && !isLockedDoor(c, keys)
}

private fun isLockedDoor(c: Char, keys: Set<Char>) = c.isUpperCase() && !keys.contains(c.toLowerCase())

private fun findStartingPoint(map: Map<Coordinate, Char>) =
    map.entries.find { it.value == '@' }!!.key

fun toMazeState(input: String): MazeState = MazeState(
    input.filter { it.isLowerCase() }.toSet(),
    parseMap(input)
)

fun parseMap(input: String): Map<Coordinate, Char> {
    return input.split('\n').mapIndexed { y, row ->
        row.mapIndexed { x, c ->
            Coordinate(x, y) to c
        }
    }.flatten().toMap()
}

data class MazeState(val keys: Set<Char>, val map: Map<Coordinate, Char>)

data class Route(val path: List<Coordinate>, val keys: Set<Char>)

class RouteComparator : Comparator<Route> {
    override fun compare(first: Route?, second: Route?): Int {
        return if(first == null || second == null) {
            0
        } else if(first.path.size == second.path.size) {
            first.keys.size.compareTo(second.keys.size)
        } else {
            first.path.size.compareTo(second.path.size)
        }
    }

}
