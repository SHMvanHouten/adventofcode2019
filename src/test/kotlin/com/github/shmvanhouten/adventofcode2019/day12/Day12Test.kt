package com.github.shmvanhouten.adventofcode2019.day12

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day12Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `for each other moon that is bigger on that axis, the velocity on that axis will change by 1`() {
            val constellation = listOf(
                Moon(Coord3d(0, 0, 0), Coord3d(0, 0, 0)),
                Moon(Coord3d(1, 1, 1), Coord3d(1, 1, 1))
            )
            val result1 = tick(constellation)
            assertThat(result1.first().velocity, equalTo(Coord3d(1, 1, 1)))

            val constellation2 = listOf(
                Moon(Coord3d(0, 0, 0), Coord3d(0, 0, 0)),
                Moon(Coord3d(1, 1, 1), Coord3d(1, 1, 1)),
                Moon(Coord3d(1, 1, 1), Coord3d(2, 2, 2))
            )
            val result2 = tick(constellation2)
            assertThat(result2.first().velocity, equalTo(Coord3d(2, 2, 2)))

        }

        @Test
        internal fun `for each other moon that is smaller on that axis the velocity on that axis decreases`() {
            val constellation = listOf(
                Moon(Coord3d(0, 0, 0), Coord3d(0, 0, 0)),
                Moon(Coord3d(-1, -1, -1), Coord3d(1, 1, 1))
            )
            val result1 = tick(constellation)
            assertThat(result1.first().velocity, equalTo(Coord3d(-1, -1, -1)))

            val constellation2 = listOf(
                Moon(Coord3d(0, 0, 0), Coord3d(0, 0, 0)),
                Moon(Coord3d(-2, -2, -2), Coord3d(1, 1, 1))
            )
            val result2 = tick(constellation2)
            assertThat(result2.first().velocity, equalTo(Coord3d(-1, -1, -1)))

            val constellation3 = listOf(
                Moon(Coord3d(0, 0, 0), Coord3d(1, 1, 1)),
                Moon(Coord3d(-2, -2, -2), Coord3d(1, 1, 1))
            )
            val result3 = tick(constellation3)
            assertThat(result3.first().velocity, equalTo(Coord3d(0, 0, 0)))
        }

        @Test
        internal fun `the moon moves the velocity it got`() {
            val constellation = listOf(
                Moon(Coord3d(0, 0, 0), Coord3d(0, 0, 0)),
                Moon(Coord3d(1, 1, 1), Coord3d(1, 1, 1))
            )
            val result1 = tick(constellation)
            assertThat("1", result1.first().position, equalTo(Coord3d(1, 1, 1)))

            val constellation2 = listOf(
                Moon(Coord3d(0, 0, 0), Coord3d(0, 0, 0)),
                Moon(Coord3d(1, 1, 1), Coord3d(1, 1, 1)),
                Moon(Coord3d(1, 1, 1), Coord3d(2, 2, 2))
            )
            val result2 = tick(constellation2)
            assertThat("2", result2.first().position, equalTo(Coord3d(2, 2, 2)))

            val constellation3 = listOf(
                Moon(Coord3d(1, 1, 1), Coord3d(0, 0, 0)),
                Moon(Coord3d(2, 2, 2), Coord3d(1, 1, 1))
            )
            val result3 = tick(constellation3)
            assertThat("3", result3.first().position, equalTo(Coord3d(2, 2, 2)))
        }

        @Test
        internal fun `for other moons the same thing applies`() {
            val constellation = listOf(
                Moon(Coord3d(0, 0, 0), Coord3d(0, 0, 0)),
                Moon(Coord3d(1, 1, 1), Coord3d(1, 1, 1))
            )
            val result1 = tick(constellation)
            assertThat(result1[1].velocity, equalTo(Coord3d(0, 0, 0)))
            assertThat(result1[1].position, equalTo(Coord3d(1, 1, 1)))
        }

        @Test
        internal fun testInput1() {
            val input = """<x=-1, y=0, z=2>
<x=2, y=-10, z=-7>
<x=4, y=-8, z=8>
<x=3, y=5, z=-1>"""
            var constellation = input.toMoons()
            repeat(10) {
                constellation = tick(constellation)
            }
            assertThat(
                constellation, equalTo(
                    listOf(
                        Moon(position = Coord3d(x = 2, y = 1, z = -3), velocity = Coord3d(x = -3, y = -2, z = 1)),
                        Moon(position = Coord3d(x = 1, y = -8, z = 0), velocity = Coord3d(x = -1, y = 1, z = 3)),
                        Moon(position = Coord3d(x = 3, y = -6, z = 1), velocity = Coord3d(x = 3, y = 2, z = -3)),
                        Moon(position = Coord3d(x = 2, y = 0, z = 4), velocity = Coord3d(x = 1, y = -1, z = -1))
                    )
                )
            )

            assertThat(calculateEnergy(constellation), equalTo(179L))

        }

        @Test
        internal fun part1() {
            val input = """<x=-9, y=-1, z=-1>
<x=2, y=9, z=5>
<x=10, y=18, z=-12>
<x=-6, y=15, z=-7>"""

            var constellation = input.toMoons()
            repeat(1000) {
                constellation = tick(constellation)
            }
            assertThat(calculateEnergy(constellation), equalTo(12644L))
        }
    }

    @Nested
    inner class Part2 {
        @Test
        internal fun `test input 1`() {
            val input = """<x=-1, y=0, z=2>
<x=2, y=-10, z=-7>
<x=4, y=-8, z=8>
<x=3, y=5, z=-1>"""
            val moons = input.toMoons()

            assertThat(countStepsToDuplicateState(moons), equalTo(2772L))
        }

        @Test
        internal fun `test input 2`() {
            val input = """<x=-8, y=-10, z=0>
<x=5, y=5, z=10>
<x=2, y=-7, z=3>
<x=9, y=-8, z=-3>"""
            val moons = input.toMoons()

            assertThat(countStepsToDuplicateState(moons), equalTo(4686774924L))
        }

        @Test
        internal fun `test input 3`() {
            val input = """<x=-9, y=-1, z=-1>
<x=2, y=9, z=5>
<x=10, y=18, z=-12>
<x=-6, y=15, z=-7>"""
            val moons = input.toMoons()

            assertThat(countStepsToDuplicateState(moons), equalTo(290314621566528L))
        }
    }

}

internal fun String.toMoons(): List<Moon> {
    return this.split('\n')
        .map { toPosition(it) }
        .map { Moon(it, Coord3d(0, 0, 0)) }
}

fun toPosition(rawPos: String): Coord3d {
    val x = rawPos.substring(rawPos.indexOf('x') + 2, rawPos.indexOf(',')).toInt()
    val y = getY(rawPos)
    val z = rawPos.substring(rawPos.indexOf('z') + 2, rawPos.indexOf('>')).toInt()
    return Coord3d(x, y, z)
}

fun getY(rawPos: String): Int {
    val substring = rawPos.substring(rawPos.indexOf('y') + 2)
    return substring.substring(0, substring.indexOf(',')).toInt()
}
