package com.github.shmvanhouten.adventofcode2019.day20

import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Coordinate
import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Direction
import com.github.shmvanhouten.adventofcode2019.day20.PortalType.INNER

fun findQuickestPath2(donutMap: Map<Coordinate, Tile>): Int {
    val unfinishedPaths = mutableListOf(Path(listOf(findStartingPoint(donutMap)), 0))
    val visited = mutableSetOf<Pair<Coordinate, Int>>()

    while (unfinishedPaths.isNotEmpty()) {
        unfinishedPaths.sortBy { it.path.size }
        val path = unfinishedPaths.removeAt(0)

        visited += path.currentLocation() to path.portalDepth

        if (isAtTheEnd(path, donutMap)) {
            return path.path.size - 1
        }

        if (isAPortalWeCanTravel(donutMap, path)) {
            unfinishedPaths += moveToOtherPortal(path, donutMap)
        } else {
            unfinishedPaths += doValidMoves2(path, donutMap, visited)
        }
    }
    throw Exception("No path found!")
}

private fun doValidMoves2(
    path: Path,
    donutMap: Map<Coordinate, Tile>,
    visited: Set<Pair<Coordinate, Int>>
): List<Path> {
    return findValidAdjacentPoints2(path.currentLocation(), path.previousLocation(), donutMap, visited, path.portalDepth)
        .map { Path(path.path + it, path.portalDepth) }
}

private fun findValidAdjacentPoints2(
    location: Coordinate,
    previousStep: Coordinate,
    donutMap: Map<Coordinate, Tile>,
    visited: Set<Pair<Coordinate, Int>>,
    portalDepth: Int
): List<Coordinate> {
    return Direction.values()
        .map { location.move(it) }
        .filter { it != previousStep }
        .filter { donutMap[it] != null }
        .filter { donutMap[it] !is Wall }
        .filter { !visited.contains(it to portalDepth) }
}

private fun isAtTheEnd(
    path: Path,
    donutMap: Map<Coordinate, Tile>
) = donutMap[path.currentLocation()] is End && path.portalDepth == 0

private fun isAPortalWeCanTravel(
    donutMap: Map<Coordinate, Tile>,
    path: Path
): Boolean {
    val tile = donutMap[path.currentLocation()]
    // care: we will now move back and forth through the same portal if we don't take precautions
    return tile is Portal && (tile.type == INNER || path.portalDepth > 0)
}

private fun moveToOtherPortal(
    path: Path,
    donutMap: Map<Coordinate, Tile>
): Path {
    val originPortal = donutMap[path.currentLocation()] as Portal
    val portalName = originPortal.name
    val destination = moveThroughPortal(donutMap, portalName, originPortal.location)
    // one extra step so we don't infinitely move through the portals.
    val oneExtraStep = findValidAdjacentPoints(destination, donutMap).first()

    val updatedPath = path.path + destination + oneExtraStep
    val updatedPortals = if (originPortal.type == INNER) {
        path.portalDepth + 1
    } else {
        path.portalDepth - 1
    }
    return Path(updatedPath, updatedPortals)
}

private fun moveThroughPortal(
    donutMap: Map<Coordinate, Tile>,
    portalName: String,
    originPortal: Coordinate
): Coordinate {
    return (donutMap
        .filter { it.value is Portal }
        .filter { (it.value as Portal).name == portalName }
        .keys.find { it != originPortal }
        ?: throw IllegalStateException("No destination portal found for $portalName at $originPortal"))
}

data class Path(val path: List<Coordinate>, val portalDepth: Int) {
    fun currentLocation(): Coordinate {
        return this.path.last()
    }

    fun previousLocation(): Coordinate {
        return if (path.size > 1) {
            this.path[path.size - 2]
        } else {
            Coordinate(-1, -1)
        }
    }
}