package com.github.shmvanhouten.adventofcode2019.day20

import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Coordinate
import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Direction

fun findQuickestPath(donutMap: Map<Coordinate, Tile>): Int {
    val visitedCoordinates = mutableSetOf<Coordinate>()
    val unfinishedPaths = mutableListOf(listOf(findStartingPoint(donutMap)))
    while (unfinishedPaths.isNotEmpty()) {
        unfinishedPaths.sortBy { it.size }
        val path = unfinishedPaths.removeAt(0)
        visitedCoordinates += path.last()

        if(isAtTheEnd(path, donutMap)) {
            return path.size - 1
        }

        when {
            isAPortalWeHaventTravelledYet(donutMap, path, visitedCoordinates) -> unfinishedPaths += moveToOtherPortal(path, donutMap)
            else -> unfinishedPaths += doValidMoves(path, donutMap, visitedCoordinates)
        }
    }
    throw Exception("No path found!")
}

private fun isAtTheEnd(
    path: List<Coordinate>,
    donutMap: Map<Coordinate, Tile>
) = donutMap[path.last()] is End

private fun moveToOtherPortal(
    path: List<Coordinate>,
    donutMap: Map<Coordinate, Tile>
): List<Coordinate> {
    val originPortal = path.last()
    val portalName = (donutMap[originPortal] as Portal).name
    val destination = moveThroughPortal(donutMap, portalName, originPortal)
    return path + destination
}

private fun moveThroughPortal(
    donutMap: Map<Coordinate, Tile>,
    portalName: String?,
    originPortal: Coordinate
): Coordinate {
   return (donutMap
       .filter { it.value is Portal }
       .filter { (it.value as Portal).name == portalName }
        .keys.find { it != originPortal }
        ?: throw IllegalStateException("No destination portal found for $portalName at $originPortal"))
}

fun doValidMoves(
    path: List<Coordinate>,
    donutMap: Map<Coordinate, Tile>,
    visitedCoordinates: MutableSet<Coordinate>
): List<List<Coordinate>> {
    return findValidAdjacentPoints(path.last(), donutMap, visitedCoordinates)
        .map { path + it }
}

fun findValidAdjacentPoints(
    location: Coordinate,
    donutMap: Map<Coordinate, Tile>,
    visitedCoordinates: Set<Coordinate> = emptySet()
): List<Coordinate> {
    return Direction.values()
        .map { location.move(it) }
        .filter { donutMap[it] != null }
        .filter { donutMap[it] !is Wall }
        .filter { !visitedCoordinates.contains(it) }
}

private fun isAPortalWeHaventTravelledYet(
    donutMap: Map<Coordinate, Tile>,
    path: List<Coordinate>,
    visitedCoordinates: MutableSet<Coordinate>
): Boolean {
    val tile = donutMap[path.last()]
    return tile is Portal && weHaventMovedThroughThisPortalYet(visitedCoordinates, donutMap, tile.name, path)
}

private fun weHaventMovedThroughThisPortalYet(
    visitedCoordinates: MutableSet<Coordinate>,
    donutMap: Map<Coordinate, Tile>,
    tile: String?,
    path: List<Coordinate>
) = !visitedCoordinates.contains(moveThroughPortal(donutMap, tile, path.last()))

fun findStartingPoint(donutMap: Map<Coordinate, Tile>): Coordinate {

    return donutMap.entries.find { it.value is Start }?.key ?: throw IllegalStateException("No starting point found!")
}

