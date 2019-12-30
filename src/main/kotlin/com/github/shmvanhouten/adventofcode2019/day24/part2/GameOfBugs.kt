package com.github.shmvanhouten.adventofcode2019.day24.part2

import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Coordinate
import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Direction

fun countBugs(eris: Eris): Int {
    return eris.tiles.flatMap { it.value.values }.count { it.hasBug }
}

fun tickTimes(eris: Eris, times: Int): Eris {
    var tickedEris = eris
    repeat(times) {
        println(tickedEris.draw())
        tickedEris = tickedEris.tick()
    }
    return tickedEris
}

data class Eris(val tiles: Map<Int, Map<Coordinate, Tile>>) {
    fun tick(): Eris {
        // todo: only extend dimension if there are bugs at the edges of the outer dimension
        val extendedTiles = addDimensionsEitherWay()
        return Eris(
            extendedTiles.values.flatMap { it.values }.map { it.tick(extendedTiles) }
                .groupBy { it.dimension }
                .mapValues { it.value.map { tile -> tile.location to tile }.toMap() }
        )
    }

    private fun addDimensionsEitherWay(): Map<Int, Map<Coordinate, Tile>> {
        val upperDimension = tiles.maxBy { it.key }!!.key + 1
        val lowerDimension = tiles.minBy { it.key }!!.key - 1
        return tiles.plus(upperDimension to emptyTileMap(upperDimension))
            .plus(lowerDimension to emptyTileMap(lowerDimension))
    }

    private fun emptyTileMap(dimension: Int): Map<Coordinate, Tile> {
        return 0.until(5).flatMap { y ->
            0.until(5).map { x -> Tile(false, Coordinate(x, y), dimension) }
        }
            .filter { it.location != Coordinate(2, 2) }
            .map { it.location to it }
            .toMap()
    }

    fun draw(): String {
        return tiles.toSortedMap()
            .map { (dimension, grid) ->
            "\nDepth: $dimension \n" + drawGrid(grid)
        }.joinToString("\n")
    }

    private fun drawGrid(grid: Map<Coordinate, Tile>): String {
        return 0.until(5).joinToString("\n") { y ->
            0.until(5).joinToString("") { x ->
                when {
                    !grid.containsKey(Coordinate(x, y)) -> " "
                    grid[Coordinate(x, y)]?.hasBug == true -> "#"
                    else -> "."
                }
            }
        }
    }

}

data class Tile(val hasBug: Boolean, val location: Coordinate, val dimension: Int) {
    fun tick(tiles: Map<Int, Map<Coordinate, Tile>>): Tile {
        val adjacentBugs = getAdjacentTiles(this, tiles).count { it.hasBug }
        val getsBug = if (hasBug) {
            adjacentBugs == 1
        } else {
            adjacentBugs == 1 || adjacentBugs == 2
        }
        return Tile(
            getsBug,
            this.location,
            this.dimension
        )
    }

    private fun getAdjacentTiles(tile: Tile, tiles: Map<Int, Map<Coordinate, Tile>>): List<Tile> {
        val surroundingTilesInThisDimension = Direction.values().map { tile.location.move(it) }
        return surroundingTilesInThisDimension
            .filter { it != Coordinate(2, 2) }
            .mapNotNull { tiles[tile.dimension]!![it] }
            .plus(getAdjacentLowerDimensionCoordinates(surroundingTilesInThisDimension, tile, tiles))
            .plus(getAdjacentHigherDimensionCoordinate(tile, tiles))
    }

    private fun getAdjacentHigherDimensionCoordinate(
        tile: Tile,
        tiles: Map<Int, Map<Coordinate, Tile>>
    ): List<Tile> {
        return listOfNotNull(
            when {
                tile.location.x == 0 -> tiles[tile.dimension + 1]?.get(Coordinate(1, 2))
                tile.location.x == 4 -> tiles[tile.dimension + 1]?.get(Coordinate(3, 2))
                else -> null
            },
            when {
                tile.location.y == 0 -> tiles[tile.dimension + 1]?.get(Coordinate(2, 1))
                tile.location.y == 4 -> tiles[tile.dimension + 1]?.get(Coordinate(2, 3))
                else -> null
            }
        )
    }

    private fun getAdjacentLowerDimensionCoordinates(
        surroundingTilesInThisDimension: List<Coordinate>,
        tile: Tile,
        tiles: Map<Int, Map<Coordinate, Tile>>
    ): List<Tile> {
        return if (surroundingTilesInThisDimension.any { it == Coordinate(2, 2) }) {
            when {
                tile.location.x == 1 -> tiles[tile.dimension - 1]?.values?.filter { it.location.x == 0 }
                tile.location.x == 3 -> tiles[tile.dimension - 1]?.values?.filter { it.location.x == 4 }
                tile.location.y == 1 -> tiles[tile.dimension - 1]?.values?.filter { it.location.y == 0 }
                tile.location.y == 3 -> tiles[tile.dimension - 1]?.values?.filter { it.location.y == 4 }
                else -> throw IllegalStateException("Found a coordinate next to 2 2 with coordinates ${tile.location}")
            }

        } else {
            emptyList()
        } ?: emptyList()
    }

    private fun isOverTheEdge(it: Coordinate) =
        it.x == -1 || it.x == 5 || it.y == -1 || it.y == 5
}
