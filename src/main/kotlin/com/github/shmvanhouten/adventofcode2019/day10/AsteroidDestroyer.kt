package com.github.shmvanhouten.adventofcode2019.day10

import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Coordinate
import java.lang.IllegalStateException
import java.lang.Math.toDegrees
import kotlin.math.abs
import kotlin.math.atan2

fun sort(station: Coordinate, asteroids: Collection<Coordinate>): List<Coordinate> {
    return asteroids.sortedBy { angleFromStation(station, it) }
}

fun angleFromStation(station: Coordinate, asteroid: Coordinate): Double {
    return when {
        station.x == asteroid.x -> if(asteroid.y < station.y) {
            0.0
        } else {
            180.0
        }
        station.y == asteroid.y -> if(asteroid.x > station.x) {
            90.0
        } else {
            270.0
        }
        else -> calculateAngle(station, asteroid)
    }

}

fun calculateAngle(station: Coordinate, asteroid: Coordinate): Double {
    return when {
        asteroid.isNorthEastOf(station) -> 0 + angle(station, asteroid)
        asteroid.isSouthEastOf(station) -> 180 - angle(station, asteroid)
        asteroid.isSouthWestOf(station) -> 180 + angle(station, asteroid)
        asteroid.isNorthWestOf(station) -> 360 - angle(station, asteroid)
        else -> throw IllegalStateException("$asteroid is at an astute angle to $station")
    }
}

fun angle(station: Coordinate, asteroid: Coordinate): Double {
    return toDegrees(atan2(
        abs((asteroid.x - station.x).toDouble()),
        abs((asteroid.y - station.y).toDouble()))
    )
}

private fun Coordinate.isNorthEastOf(station: Coordinate): Boolean {
    return this.y < station.y && this.x > station.x
}

private fun Coordinate.isSouthEastOf(station: Coordinate): Boolean {
    return this.y > station.y && this.x > station.x
}

private fun Coordinate.isSouthWestOf(station: Coordinate): Boolean {
    return this.y > station.y && this.x < station.x
}

private fun Coordinate.isNorthWestOf(station: Coordinate): Boolean {
    return this.y < station.y && this.x < station.x
}
