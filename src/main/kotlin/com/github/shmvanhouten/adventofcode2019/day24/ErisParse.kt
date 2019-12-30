package com.github.shmvanhouten.adventofcode2019.day24

import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Coordinate

fun toEris(input: String): Eris {
    return Eris(
        parseTiles(input)
    )
}

private fun parseTiles(input: String): Map<Coordinate, Tile> {
    return input.split('\n')
        .mapIndexed { y, row ->
            row.mapIndexed { x, tile ->
                totTile(tile, x, y)
            }
        }.flatten()
        .map { it.location to it }
        .toMap()
}

private fun totTile(tile: Char, x: Int, y: Int) =
    if (tile == '.') {
        Tile(false, Coordinate(x, y))
    } else {
        Tile(true, Coordinate(x, y))
    }
