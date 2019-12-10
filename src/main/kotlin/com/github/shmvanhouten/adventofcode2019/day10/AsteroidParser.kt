package com.github.shmvanhouten.adventofcode2019.day10

import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Coordinate

fun parse(input: String): Set<Coordinate> {
    return input.split('\n').mapIndexed{i, row -> parseRow(i, row)}.flatten().toSet()
}

fun parseRow(y: Int, row: String): List<Coordinate> {
    return row
        .mapIndexed { x, c -> x to c }
        .filter { (_, c) -> c == '#' }
        .map { (x, _) -> Coordinate(x,y) }
}
