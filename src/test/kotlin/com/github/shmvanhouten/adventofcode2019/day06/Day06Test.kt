package com.github.shmvanhouten.adventofcode2019.day06

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import util.FileReader.readFile

class Day06Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `just B around COM is 1 orbit`() {
            val input = """COM)B"""
            assertThat(countOrbits(input), equalTo(1))
        }

        @Test
        internal fun `A and B both orbiting COM is 2 orbits`() {
            val input = """COM)A
                |COM)B
            """.trimMargin()
            assertThat(countOrbits(input), equalTo(2))
        }

        @Test
        internal fun `A B and C orbiting COM is 3 orbits`() {
            val input = """COM)A
                |COM)B
                |COM)C
            """.trimMargin()
            assertThat(countOrbits(input), equalTo(3))
        }

        @Test
        internal fun `A orbiting COM and B orbiting A is a combined 3 orbits`() {
            val input = """COM)A
                |A)B
            """.trimMargin()
            assertThat(countOrbits(input), equalTo(3))
        }

        @Test
        internal fun `A and B orbiting COM and C orbiting A is a combined 4 orbits`() {
            val input = """COM)A
                |COM)B
                |A)C
            """.trimMargin()
            assertThat(countOrbits(input), equalTo(4))
        }

        @Test
        internal fun `A orbiting COM and B and C orbiting A is a combined 5 orbits`() {
            val input = """COM)A
                |A)B
                |A)C
            """.trimMargin()
            assertThat(countOrbits(input), equalTo(5))
        }

        @Test
        internal fun `A orbiting COM, B orbiting A and C orbiting B results in a combined 6 orbits`() {
            val input = """COM)A
                |A)B
                |B)C
            """.trimMargin()
            assertThat(countOrbits(input), equalTo(6))
        }

        @Test
        internal fun `its orbits all the way down`() {
            val input = """COM)B
                                 |B)C
                                 |C)D
                                 |D)E
                                 |E)F
                                 |B)G
                                 |G)H
                                 |D)I
                                 |E)J
                                 |J)K
                                 |K)L
                                    """.trimMargin()
            assertThat(countOrbits(input), equalTo(42))
        }

        @Test
        internal fun `part 1`() {
//            val input = readFile("/input-day06.txt")
//            assertThat(countOrbits(input), equalTo(358244))
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `If both YOU and SAN orbit the same planet, it takes 0 orbital transfers`() {
            val input = """COM)YOU
                                 |COM)SAN
                               """.trimMargin()
            assertThat(shortestPath(input), equalTo(0))
        }

        @Test
        internal fun `if YOU orbit a planet that orbits the planet SAN orbits, it takes 1 orbital transfer`() {
            val input = """COM)A
                                 |A)YOU
                                 |COM)SAN
                               """.trimMargin()
            assertThat(shortestPath(input), equalTo(1))
        }

        @Test
        internal fun `two steps`() {
            val input = """COM)A
                                 |A)YOU
                                 |COM)B
                                 |B)SAN
                               """.trimMargin()
            assertThat(shortestPath(input), equalTo(2))
        }

        @Test
        internal fun `three steps`() {
            val input = """COM)A
                                 |A)B
                                 |B)C
                                 |C)YOU
                                 |COM)SAN
                               """.trimMargin()
            assertThat(shortestPath(input), equalTo(3))
        }

        @Test
        internal fun `the common planet is higher up the chain`() {
            val input = """COM)A
                                 |A)YOU
                                 |A)B
                                 |B)SAN
                               """.trimMargin()
            assertThat(shortestPath(input), equalTo(1))
        }

        @Test
        internal fun `test input`() {
            val input = """COM)B
                                  |B)C
                                  |C)D
                                  |D)E
                                  |E)F
                                  |B)G
                                  |G)H
                                  |D)I
                                  |E)J
                                  |J)K
                                  |K)L
                                  |K)YOU
                                  |I)SAN
                                  """.trimMargin()
            assertThat(shortestPath(input), equalTo(4))
        }

        @Test
        internal fun `part 2`() {
            val input = readFile("/input-day06.txt")
            assertThat(shortestPath(input), equalTo(517))
        }

    }

}
