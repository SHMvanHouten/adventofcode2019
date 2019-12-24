package com.github.shmvanhouten.adventofcode2019.day18

import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Coordinate
import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Direction

fun stepsRequired(input: String): Int {

    val (keys, vectors) = findPathBetweenEachPoint(input)

    val unfinishedRoutes = mutableSetOf(Route(listOf('@'), 0))
    var fastestRoute: Route? = null

    while (unfinishedRoutes.isNotEmpty()) {
        val route = unfinishedRoutes.minBy { it.length }!!
        val (path, length) = route
        removeDuplicatePaths(unfinishedRoutes, path)
        val unvisitedKeys = keys.toSet() - getKeys(path)
        if (unvisitedKeys.isEmpty()) {
            fastestRoute = route
        } else {
            unfinishedRoutes += listAvailableVectors(unvisitedKeys, path, vectors, length)
        }
        unfinishedRoutes.removeIf { it.length >= fastestRoute?.length ?: Int.MAX_VALUE }
    }

    return fastestRoute?.length ?: throw  Exception("No routes found")
}

private fun listAvailableVectors(
    unvisitedKeys: Set<Char>,
    path: List<Char>,
    vectors: MutableMap<Char, Map<Char, Vector>>,
    length: Int
): List<Route> {
    return unvisitedKeys
        .map { it to findVector(path.last(), it, vectors) }
        .filter { hasKeysForAllDoors(it.second, path) }
        .filter { (it.second.keys - it.first).all { key -> getKeys(path).contains(key) } }
        .map { (key, vector) -> Route(path + key, vector.path.size + length) }
}

private fun removeDuplicatePaths(
    unfinishedRoutes: MutableSet<Route>,
    path: List<Char>
) {
    unfinishedRoutes.removeIf { it.path.last() == path.last() && it.path.sorted() == path.sorted() }
}

fun hasKeysForAllDoors(
    vector: Vector,
    path: List<Char>
) = vector.doors.intersect(getKeys(path).map { it.toUpperCase() }).size == vector.doors.size

fun getKeys(path: List<Char>) =
    path.filter { it != '@' }.toSet()

fun findVector(c1: Char, c2: Char, vectors: MutableMap<Char, Map<Char, Vector>>): Vector {
    val chars = listOf(c1, c2).sorted()
    return vectors[chars[0]]!![chars[1]] ?: throw IllegalStateException("Could not find vector for keys $c1, $c2")
}

fun findPathBetweenEachPoint(input: String): KeysToVectorBetweenKeys {
    val (availableKeys, map) = toMazeState(input)
    val remainingKeys = (listOf('@') + availableKeys.sorted()).toMutableList()
    val vectors = mutableMapOf<Char, Map<Char, Vector>>()
    while (remainingKeys.size > 1) {
        val key = remainingKeys.removeAt(0)
        vectors[key] = findPathBetweenPointAndAllOtherPoints(key, remainingKeys, map)
    }

    return availableKeys to vectors
}

private fun findPathBetweenPointAndAllOtherPoints(
    origin: Char,
    destinations: MutableList<Char>,
    map: Map<Coordinate, Char>
): Map<Char, Vector> {
    val originLocation = findLocationOfKey(map, origin)
    return destinations
        .map { it to findPathBetweenPoints(originLocation, findLocationOfKey(map, it), map) }
        .toMap()
}

private fun findPathBetweenPoints(
    origin: Coordinate,
    target: Coordinate,
    map: Map<Coordinate, Char>
): Vector {
    val unfinishedRoutes = mutableListOf(Vector(listOf(origin), emptySet(), emptySet()))
    val visitedCoordinates = mutableSetOf<Coordinate>()
    while (unfinishedRoutes.isNotEmpty()) {
        unfinishedRoutes.sortBy { it.path.size }
        val vector = unfinishedRoutes.removeAt(0)
        val currentLocation = vector.path.last()
        if (currentLocation == target) {
            return Vector(vector.path.tail(), vector.doors, vector.keys)
        }
        visitedCoordinates.add(currentLocation)

        unfinishedRoutes += Direction.values()
            .map { currentLocation.move(it) }
            .filter { isAValidMove(it, map) }
            .filter { !visitedCoordinates.contains(it) }
            .map { toVector(it, vector, map) }

    }
    throw IllegalStateException("could not found route between $origin and $target")
}

private fun toVector(
    coordinate: Coordinate,
    vector: Vector,
    map: Map<Coordinate, Char>
): Vector {
    val doors = addNewDoorIfCurrentCoordinateIsADoor(coordinate, vector.doors, map)
    val keys = addNewkeysIfCurrentCoordinateIsAKey(coordinate, vector.keys, map)
    return Vector(vector.path + coordinate, doors, keys)
}

fun addNewkeysIfCurrentCoordinateIsAKey(
    coordinate: Coordinate,
    keys: Set<Char>,
    map: Map<Coordinate, Char>
): Set<Char> {
    return if (isAKey(coordinate, map)) {
        keys + map[coordinate]!!
    } else {
        keys
    }
}

private fun addNewDoorIfCurrentCoordinateIsADoor(
    coordinate: Coordinate,
    doors: Set<Char>,
    map: Map<Coordinate, Char>
): Set<Char> {
    return if (isADoor(coordinate, map)) {
        doors + map[coordinate]!!
    } else {
        doors
    }
}

fun isAKey(coordinate: Coordinate, map: Map<Coordinate, Char>): Boolean {
    return map[coordinate]?.isLowerCase()
        ?: throw IllegalStateException("Could not find anything at coordinate $coordinate")
}

private fun isADoor(coordinate: Coordinate, map: Map<Coordinate, Char>): Boolean {
    return map[coordinate]?.isUpperCase()
        ?: throw IllegalStateException("Could not find anything at coordinate $coordinate")
}

fun isAValidMove(
    coordinate: Coordinate,
    map: Map<Coordinate, Char>
): Boolean {
    return map[coordinate] != null && map[coordinate] != '#'
}

private fun findLocationOfKey(
    map: Map<Coordinate, Char>,
    key: Char
) = map.entries.find { it.value == key }?.key ?: throw IllegalStateException("No coordinate found for key $key")

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

private fun <E> List<E>.tail(): List<E> {
    return this.subList(0, this.size - 1)
}

data class MazeState(val keys: Set<Char>, val map: Map<Coordinate, Char>)

data class Vector(val path: List<Coordinate>, val doors: Set<Char>, val keys: Set<Char>)

data class Route(val path: List<Char>, val length: Int)

