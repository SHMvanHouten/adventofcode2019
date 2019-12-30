package com.github.shmvanhouten.adventofcode2019.day24

import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Coordinate
import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Direction
import java.math.BigInteger
import java.math.BigInteger.ZERO

fun evolveUntilRepeatState(originalEris: Eris): Eris {
    val erisStates = mutableSetOf<Eris>()
    generateSequence(originalEris){it.tick()}
        .forEach {
            if (erisStates.contains(it)) {
                return it
            } else {
                erisStates.add(it)
            }
        }
    throw IllegalStateException()
}

data class Eris(val tiles: Map<Coordinate, Tile>) {
    fun tick(): Eris {
        return Eris(
            tiles.values.map { it.tick(tiles) }
                .map{it.location to it}
                .toMap()
        )
    }

    fun calculateBiodiversity(): BigInteger {
        return tiles.values.asSequence().sortedWith(TileComparator())
            .mapIndexed { i, tile -> i to tile.hasBug }
            .filter { it.second }
            .map { BigInteger.valueOf(2).pow(it.first) }
            .fold(ZERO) { acc, bigInteger -> acc + bigInteger }
    }
}

class TileComparator : Comparator<Tile> {
    override fun compare(o1: Tile?, o2: Tile?): Int {
        if(o1 == null || o2 == null) {
            return 0
        }
        val location1 = o1.location
        val location2 = o2.location
        return if(location1.y == location2.y) {
            location1.x.compareTo(location2.x)
        } else {
            location1.y.compareTo(location2.y)
        }
    }

}

data class Tile(val hasBug: Boolean, val location: Coordinate) {
    fun tick(tiles: Map<Coordinate, Tile>): Tile {
        val adjacentBugs = getAdjacentTiles(this, tiles).count { it.hasBug }
        val getsBug = if(hasBug) {
            adjacentBugs == 1
        } else {
            adjacentBugs == 1 || adjacentBugs == 2
        }
        return Tile(
            getsBug,
            this.location
        )
    }

    private fun getAdjacentTiles(tile: Tile, tiles: Map<Coordinate, Tile>): List<Tile> {
        return Direction.values()
            .map { tile.location.move(it) }
            .mapNotNull { tiles[it] }
    }
}
