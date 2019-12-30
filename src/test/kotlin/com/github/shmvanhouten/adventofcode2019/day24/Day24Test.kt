package com.github.shmvanhouten.adventofcode2019.day24

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.math.BigInteger

@Suppress("ClassName")
class Day24Test {

    @Nested
    inner class Part1 {

        @Nested
        inner class `On a two by two Eris` {

            @Test
            internal fun `empty eris has 0 biodiversity rating`() {
                val input = """
                |..
                |..
            """.trimMargin()
                val eris = toEris(input)

                assertThat(eris.calculateBiodiversity(), equalTo(BigInteger.ZERO))
            }

            @Test
            internal fun `eris with bug at 1,1 has 1 biodiversity rating`() {
                val input = """
                |#.
                |..
            """.trimMargin()
                val eris = toEris(input)

                assertThat(eris.calculateBiodiversity(), equalTo(BigInteger.ONE))
            }

            @Test
            internal fun `eris with bug at 1,2 has 2 biodiversity rating`() {
                val input = """
                |.#
                |..
            """.trimMargin()
                val eris = toEris(input)

                assertThat(eris.calculateBiodiversity(), equalTo(BigInteger.valueOf(2)))
            }

            @Test
            internal fun `eris with bug at 2,2 has 2^3 biodiversity rating`() {
                val input = """
                |..
                |.#
            """.trimMargin()
                val eris = toEris(input)

                assertThat(eris.calculateBiodiversity(), equalTo(BigInteger.valueOf(2).pow(3)))
            }

            @Test
            internal fun `eris with bug at 1,1 and 2,2 has 9 biodiversity rating`() {
                val input = """
                |#.
                |.#
            """.trimMargin()
                val eris = toEris(input)

                assertThat(eris.calculateBiodiversity(), equalTo(BigInteger.valueOf(9)))
            }
        }

        @Test
        internal fun `an empty space stays empty if no bugs surround it`() {
            val input = """
                |..
                |..
            """.trimMargin()
            val eris = toEris(input)

            assertThat(eris.tick(), equalTo(eris))
        }

        @Test
        internal fun `a tile becomes or remains populated if 1 bug is next to it`() {
            val input = """
                |#.
                |#.
            """.trimMargin()
            val eris = toEris(input)

            val expected = toEris(
                """
                |##
                |##
            """.trimMargin()
            )

            assertThat(eris.tick(), equalTo(expected))
        }

        @Test
        internal fun `a bug dies if no bugs are next to it`() {
            val input = """
                |#.
                |..
            """.trimMargin()
            val eris = toEris(input)

            val expected = toEris(
                """
                |.#
                |#.
            """.trimMargin()
            )

            assertThat(eris.tick(), equalTo(expected))
        }

        @Test
        internal fun `a bug dies if there are two bugs next to it`() {
            val input = """
                |##
                |#.
            """.trimMargin()
            val eris = toEris(input)

            val expected = toEris(
                """
                |.#
                |##
            """.trimMargin()
            )

            assertThat(eris.tick(), equalTo(expected))
        }

        @Test
        internal fun `get the first state to appear twice`() {
            val originalEris = toEris(
                """
                |#.
                |.#
            """.trimMargin()
            )

            val expected = toEris(
                """
                |.#
                |#.
            """.trimMargin()
            )

            val ticked = originalEris.tick()
            assertThat(ticked, equalTo(expected))
            assertThat(ticked.tick(), equalTo(originalEris))
            // so:

            assertThat(evolveUntilRepeatState(originalEris), equalTo(originalEris))
        }

        @Test
        internal fun `test input 1`() {
            val originalEris = toEris(
                """ 
            |....#
            |#..#.
            |#..##
            |..#..
            |#....
            |""".trimMargin()
            )

            val expectedEris = toEris(
                """
                |.....
                |.....
                |.....
                |#....
                |.#...
                |""".trimMargin()
            )

            val resultEris = evolveUntilRepeatState(originalEris)
            assertThat(resultEris, equalTo(expectedEris))
            assertThat(resultEris.calculateBiodiversity(), equalTo(BigInteger.valueOf(2129920)))
        }

        @Test
        internal fun `part 1`() {
            val originalEris = toEris(
                """
                |#.#..
                |.###.
                |...#.
                |###..
                |#....
            |""".trimMargin()
            )

            val resultEris = evolveUntilRepeatState(originalEris)
            assertThat(resultEris.calculateBiodiversity(), equalTo(BigInteger.valueOf(18375063)))
        }
    }
}
