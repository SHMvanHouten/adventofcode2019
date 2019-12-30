package com.github.shmvanhouten.adventofcode2019.day24.part2

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

@Suppress("ClassName")
class Day24_Part2_Test {

    @Test
    internal fun `test input 2`() {
        val originalEris = toEris(
            """
               |....#
               |#..#.
               |#..##
               |..#..
               |#....
               |""".trimMargin()
        )
        val tickedEris= tickTimes(originalEris, 10)
        assertThat(countBugs(tickedEris), equalTo(99))
    }

    @Test
    internal fun part2() {
        val originalEris = toEris(
            """
                |#.#..
                |.###.
                |...#.
                |###..
                |#....
            |""".trimMargin()
        )
        val tickedEris= tickTimes(originalEris, 200)
        assertThat(countBugs(tickedEris), equalTo(1959))
    }
}