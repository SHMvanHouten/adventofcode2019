package com.github.shmvanhouten.adventofcode2019.day20

import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Coordinate
import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Direction

fun findQuickestPath(donutMap: Map<Coordinate, String>): Int {
    val visitedCoordinates = mutableSetOf<Coordinate>()
    val unfinishedPaths = mutableListOf(listOf(findStartingPoint(donutMap)))
    while (unfinishedPaths.isNotEmpty()) {
        unfinishedPaths.sortBy { it.size }
        val path = unfinishedPaths.removeAt(0)
        visitedCoordinates += path.last()

        when {
            isAtTheEnd(path, donutMap) -> return path.size
            isAPortalWeHaventTravelledYet(donutMap, path, visitedCoordinates) -> unfinishedPaths += moveToOtherPortal(path, donutMap)
            else -> unfinishedPaths += doValidMoves(path, donutMap, visitedCoordinates)
        }
    }
    throw Exception("No path found!")
}

private fun isAtTheEnd(
    path: List<Coordinate>,
    donutMap: Map<Coordinate, String>
) = donutMap[path.last()] == "ZZ"

private fun moveToOtherPortal(
    path: List<Coordinate>,
    donutMap: Map<Coordinate, String>
): List<Coordinate> {
    val originPortal = path.last()
    val portalName = donutMap[originPortal]
    val destination = moveThroughPortal(donutMap, portalName, originPortal)
    return path + destination
}

private fun moveThroughPortal(
    donutMap: Map<Coordinate, String>,
    portalName: String?,
    originPortal: Coordinate
): Coordinate {
   return (donutMap.filter { it.value == portalName }
        .keys.find { it != originPortal }
        ?: throw IllegalStateException("No destination portal found for $portalName at $originPortal"))
}

fun doValidMoves(
    path: List<Coordinate>,
    donutMap: Map<Coordinate, String>,
    visitedCoordinates: MutableSet<Coordinate>
): List<List<Coordinate>> {
    return findValidAdjacentPoints(path.last(), donutMap, visitedCoordinates)
        .map { path + it }
}

fun findValidAdjacentPoints(
    location: Coordinate,
    donutMap: Map<Coordinate, String>,
    visitedCoordinates: Set<Coordinate> = emptySet()
): List<Coordinate> {
    return Direction.values()
        .map { location.move(it) }
        .filter { donutMap[it] != null }
        .filter { donutMap[it] != "#" && donutMap[it] != "AA" }
        .filter { !visitedCoordinates.contains(it) }
}

private fun isAPortalWeHaventTravelledYet(
    donutMap: Map<Coordinate, String>,
    path: List<Coordinate>,
    visitedCoordinates: MutableSet<Coordinate>
): Boolean {
    val tile = donutMap[path.last()]
    return tile != "." && weHaventMovedThroughThisPortalYet(visitedCoordinates, donutMap, tile, path)
}

private fun weHaventMovedThroughThisPortalYet(
    visitedCoordinates: MutableSet<Coordinate>,
    donutMap: Map<Coordinate, String>,
    tile: String?,
    path: List<Coordinate>
) = !visitedCoordinates.contains(moveThroughPortal(donutMap, tile, path.last()))

fun findStartingPoint(donutMap: Map<Coordinate, String>): Coordinate {
    val startingPortal =
        donutMap.entries.find { it.value == "AA" }?.key ?: throw IllegalStateException("No starting point found!")

    return findValidAdjacentPoints(startingPortal, donutMap).first()
}

