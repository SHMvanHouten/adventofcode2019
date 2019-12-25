package com.github.shmvanhouten.adventofcode2019.day20

import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Coordinate
import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Direction.EAST
import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Direction.SOUTH

fun parsePortalDonut(input: String): Map<Coordinate, String> {
    val rawMap = input.split('\n')
        .mapIndexed { y, row ->
            row.mapIndexed { x, c ->
                if (c == ' ') {
                    null
                } else {
                    Coordinate(x, y) to c
                }
            }.filterNotNull()
        }.flatten()
    return withPortals(rawMap)
}

fun withPortals(rawMap: List<Pair<Coordinate, Char>>): Map<Coordinate, String> {
    val withoutPortals = rawMap.filter { !it.second.isLetter() }.map { it.first to it.second.toString() }
    val portals = rawMap
        .filter { it.second.isLetter() }
        .mapNotNull {
            val (coordinate, c) = it
            val adjacent = findAdjacentChar(coordinate, rawMap.filter { it.second.isLetter() }.toMutableSet())
            if (isSecondCharOfPortal(adjacent)) {
                findFirstOpenTileNextToPortalName(
                    coordinate,
                    adjacent!!.first,
                    withoutPortals.toMap()
                ) to c.toString() + adjacent!!.second

            } else {
                null
            }
        }
    return (withoutPortals + portals).toMap()
}

fun findFirstOpenTileNextToPortalName(
    first: Coordinate,
    second: Coordinate,
    map: Map<Coordinate, String>
): Coordinate {
    val adjacentToFirst = findValidAdjacentPoints(first, map)
    return if (adjacentToFirst.isEmpty()) {
        findValidAdjacentPoints(second, map).first()
    } else {
        adjacentToFirst.first()
    }
}

private fun isSecondCharOfPortal(adjacent: Pair<Coordinate, Char>?) =
    adjacent != null

fun findAdjacentChar(
    coordinate: Coordinate,
    portalParts: MutableSet<Pair<Coordinate, Char>>
): Pair<Coordinate, Char>? {
    return portalParts.find { it.first == coordinate.move(EAST) } ?: portalParts.find {
        it.first == coordinate.move(
            SOUTH
        )
    }
}

