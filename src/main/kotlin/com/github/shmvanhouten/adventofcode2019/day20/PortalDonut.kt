package com.github.shmvanhouten.adventofcode2019.day20

import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Coordinate
import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Direction.EAST
import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Direction.SOUTH
import com.github.shmvanhouten.adventofcode2019.day20.PortalType.INNER
import com.github.shmvanhouten.adventofcode2019.day20.PortalType.OUTER

fun parsePortalDonut(input: String): Map<Coordinate, Tile> {
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

fun withPortals(rawMap: List<Pair<Coordinate, Char>>): Map<Coordinate, Tile> {
    val withoutPortals = rawMap
        .filter { !it.second.isLetter() }
        .map { it.first to toTile(it.first, it.second) }

    val portalParts = rawMap.filter { it.second.isLetter() }.toMutableSet()
    val portals = rawMap
        .filter { it.second.isLetter() }
        .mapNotNull {
            val (coordinate, c) = it
            val adjacent = findAdjacentChar(coordinate, portalParts)
            if (isSecondCharOfPortal(adjacent)) {
                val name = c.toString() + adjacent!!.second
                val portal = toPortal(coordinate, adjacent, withoutPortals, name)
                portal.location to portal
            } else {
                null
            }
        }
    return (withoutPortals + portals).toMap()
}

private fun toPortal(
    coordinate: Coordinate,
    adjacent: Pair<Coordinate, Char>,
    withoutPortals: List<Pair<Coordinate, Tile>>,
    name: String
): Tile {
    // We assign the open tile next to the Portal name as the Portal
    // because that makes us not take unnecessary steps
    val openTile = findFirstOpenTileNextToPortalName(
        coordinate,
        adjacent.first,
        withoutPortals.toMap()
    )
    return when (name) {
        "AA" -> Start(openTile)
        "ZZ" -> End(openTile)
        else -> Portal(
            location = openTile,
            name = name,
            type = assessPortalType(coordinate, withoutPortals.map { it.first })
        )
    }
}

fun assessPortalType(coordinate: Coordinate, map: List<Coordinate>): PortalType {
    return if (isSurroundedByTiles(coordinate, map)) {
        INNER
    } else {
        OUTER
    }
}

private fun isSurroundedByTiles(
    coordinate: Coordinate,
    map: List<Coordinate>
): Boolean {
    // checking on row only is sufficient
    val allInSameRow = map.filter { it.y == coordinate.y }
    return allInSameRow.any { it.x > coordinate.x } && allInSameRow.any { it.x < coordinate.x }
}

private fun toTile(location: Coordinate, c: Char): Tile {
    return when (c) {
        '#' -> Wall(location)
        '.' -> Hallway(location)
        else -> throw IllegalStateException("Unknown tile: $c at $location")
    }
}

fun findFirstOpenTileNextToPortalName(
    first: Coordinate,
    second: Coordinate,
    map: Map<Coordinate, Tile>
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

interface Tile {
    val location: Coordinate
}

data class Wall(override val location: Coordinate) : Tile
data class Hallway(override val location: Coordinate) : Tile
data class Start(override val location: Coordinate) : Tile
data class End(override val location: Coordinate) : Tile

data class Portal(
    override val location: Coordinate,
    val name: String,
    val type: PortalType
) : Tile

enum class PortalType {
    INNER,
    OUTER
}

