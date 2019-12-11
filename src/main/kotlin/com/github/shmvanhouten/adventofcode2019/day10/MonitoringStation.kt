package com.github.shmvanhouten.adventofcode2019.day10

import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Coordinate
import kotlin.math.abs

fun findMonitoringStation(locations: Set<Coordinate>): Station {
    val stations = locations.map { it to findLocationsItCanSee(it, locations - it) }
        .map { toStation(it) }
    return stations
        .maxBy { it.sees.size } ?: throw IllegalStateException("empty collection provided: $locations")

}

fun findLocationsItCanSee(location: Coordinate, locations: Set<Coordinate>): Set<Coordinate> {

    val sees = mutableSetOf<Coordinate>()
    for (coordinate in locations.sortedBy { it.distanceFrom(location) }) {
        if (noBlockages(coordinate, location, sees)) {
            sees += coordinate
        }
    }
    return sees
}

fun noBlockages(coordinate1: Coordinate, coordinate2: Coordinate, sees: Set<Coordinate>): Boolean {
    return sees.none { it.blocks(coordinate1, coordinate2) }
}

fun toStation(pair: Pair<Coordinate, Set<Coordinate>>): Station {
    return Station(pair.first, pair.second)
}

data class Station(val location: Coordinate, val sees: Set<Coordinate>)

private fun Coordinate.distanceFrom(other: Coordinate): Int {
    return abs(this.x - other.x) + abs(this.y - other.y)
}

private fun Coordinate.blocks(coordinate1: Coordinate, coordinate2: Coordinate): Boolean {
    return this.isBetweenOnSameAxis( coordinate1, coordinate2) ||
            (this.isBetween(coordinate1, coordinate2)
            && areOnALine(listOf(this, coordinate1, coordinate2).sortedBy { it.x }))
}

private fun Coordinate.isBetweenOnSameAxis(coordinate1: Coordinate, coordinate2: Coordinate): Boolean {
    val coordinates = listOf(this, coordinate1, coordinate2)
    return (coordinates.all { it.x == this.x }&& coordinates.sortedBy { it.y }[1] == this)
            || (coordinates.all{it.y == this.y }&& coordinates.sortedBy { it.x }[1] == this)
}

private fun Coordinate.isBetween(coordinate1: Coordinate, coordinate2: Coordinate): Boolean {
    val coordinates = listOf(this, coordinate1, coordinate2)
    return coordinates.sortedBy { it.y }[1] == this
            && coordinates.sortedBy { it.x }[1] == this
}

fun areOnALine(coords: List<Coordinate>): Boolean {
    return !anyAreOnSameAxis(coords) && areAllOnALine(coords)
}

private fun anyAreOnSameAxis(coordinates: List<Coordinate>): Boolean {

    return  someAreOnSameX(coordinates) || someAreOnSameY(coordinates)
}

private fun someAreOnSameX(coordinates: List<Coordinate>): Boolean {
    val sortedX = coordinates.map{it.x}.sorted()
    return sortedX[0] == sortedX[1] || sortedX[1] == sortedX[2]
}

private fun someAreOnSameY(coordinates: List<Coordinate>): Boolean {
    val sortedByX = coordinates.map{it.y}.sorted()
    return sortedByX[0] == sortedByX[1] || sortedByX[1] == sortedByX[2]
}

private fun areAllOnALine(coords: List<Coordinate>): Boolean {
    return abs(coords[1].x - coords[2].x) / abs(coords[1].x - coords[0].x).toFloat() ==
            abs(coords[1].y - coords[2].y) / abs(coords[1].y - coords[0].y).toFloat()
}
