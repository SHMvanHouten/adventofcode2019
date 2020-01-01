package com.github.shmvanhouten.adventofcode2019.day17

import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Coordinate

fun findIntersections(input: String): List<Coordinate> {
    val scaffold = parseScaffold(input)
    return scaffold
        .filter { it.getNeighbours().all { neighbour -> isAScaffold(neighbour, scaffold) } }

}

fun parseScaffold(input: String): Set<Coordinate> {
    return input.split('\n')
        .asSequence()
        .mapIndexed { y, line ->
            line.mapIndexed { x, c ->
                Coordinate(x, y) to c
            }
                .filter { it.second == '#' }
                .map { it.first }
        }
        .flatten()
        .toSet()
}

fun isAScaffold(neighbour: Coordinate, scaffold: Set<Coordinate>): Boolean {
    return scaffold.contains(neighbour)
}

fun findStartingPoint(input: String): Coordinate {
    return input.split('\n')
        .mapIndexed { y, line ->
            line.mapIndexed { x, c ->
                Coordinate(x, y) to c
            }
                .filter { it.second == '^' }
                .map { it.first }
        }.flatten()
        .first()
}
