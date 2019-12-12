package com.github.shmvanhouten.adventofcode2019.day12

data class Coord3d(val x: Int, val y: Int, val z: Int) {
    operator fun plus(other: Coord3d): Coord3d {
        return Coord3d(this.x + other.x, this.y + other.y, this.z + other.z)
    }
}