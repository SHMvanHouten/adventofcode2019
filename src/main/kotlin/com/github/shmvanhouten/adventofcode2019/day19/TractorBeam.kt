package com.github.shmvanhouten.adventofcode2019.day19

import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Coordinate
import com.github.shmvanhouten.adventofcode2019.day02.Computer
import com.github.shmvanhouten.adventofcode2019.day02.IntCode

fun scanForAffectedPoints(intCode: IntCode): List<Coordinate> {
    return 0.until(50).flatMap { y ->
        0.until(50).map { x ->
            Coordinate(x, y)
        }
    }.filter { coordinate ->
        isAffected(intCode, coordinate)
    }
}

fun findFirst100By100Box(intCode: IntCode): Coordinate {
    val bottomLeft = findBottomLeftCoordinateOf100By100Box(intCode)
    return Coordinate(bottomLeft.x, bottomLeft.y - 99)
}

private fun findBottomLeftCoordinateOf100By100Box(intCode: IntCode): Coordinate {
    var minx = 0
    return generateSequence(0) { i -> i + 1 }
        .map { y ->
            minx = findFirstInRowAffectedByBeam(intCode, minx, y)
            Coordinate(minx, y)
        }.first {
            isBottomLeftCornerOfCompletelyAffectedBox(intCode, it)
        }
}

private fun isBottomLeftCornerOfCompletelyAffectedBox(
    intCode: IntCode,
    it: Coordinate
) = isAffected(intCode, Coordinate(it.x + 99, it.y - 99)) &&
        isAffected(intCode, Coordinate(it.x, it.y - 99))

private fun findFirstInRowAffectedByBeam(
    intCode: IntCode,
    minx: Int,
    y: Int
): Int {
    var minx1 = minx
    while (!isAffected(intCode, Coordinate(minx1, y))) {
        minx1++
    }
    return minx1
}

private fun isAffected(
    intCode: IntCode,
    coordinate: Coordinate
): Boolean {
    val computer = Computer(intCode)
    computer.input(coordinate.x.toLong())
    computer.input(coordinate.y.toLong())
    return computer.output.poll() == 1L
}
