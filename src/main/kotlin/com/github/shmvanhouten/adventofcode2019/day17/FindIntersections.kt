package com.github.shmvanhouten.adventofcode2019.day17

import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Coordinate

fun findIntersections(input: String): List<Coordinate> {
    val map = input.split('\n')
        .mapIndexed { y, line ->
            line.mapIndexed { x, c ->
                Coordinate(x, y) to c
            }
        }.flatten().toMap()
    return map.filter { it.value == '#' }
        .filter { it.key.getNeighbours().all { neighbour -> isAScaffold(neighbour, map) } }
        .keys.toList()

}

fun isAScaffold(neighbour: Coordinate, map: Map<Coordinate, Char>): Boolean {
    return map.containsKey(neighbour) && map[neighbour] == '#'
}
