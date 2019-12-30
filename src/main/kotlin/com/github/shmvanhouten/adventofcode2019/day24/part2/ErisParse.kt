package com.github.shmvanhouten.adventofcode2019.day24.part2

import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Coordinate

fun toEris(input: String): Eris {
    return Eris(
        parseTiles(input)
    )
}

private fun parseTiles(input: String): Map<Int, Map<Coordinate, Tile>> {
    return listOf(0 to parseSingleDimension(input))
        .toMap()
}

private fun parseSingleDimension(input: String): Map<Coordinate, Tile> {
    return input.split('\n')
        .mapIndexed { y, row ->
            row.mapIndexed { x, tile ->
                toTile(tile, x, y)
            }
        }.flatten()
        .filter { it.location != Coordinate(2, 2) }
        .map { it.location to it }
        .toMap()
}

private fun toTile(tile: Char, x: Int, y: Int) =
    if (tile == '.') {
        Tile(false, Coordinate(x, y), 0)
    } else {
        Tile(true, Coordinate(x, y), 0)
    }
