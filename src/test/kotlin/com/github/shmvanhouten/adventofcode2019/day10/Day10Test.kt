package com.github.shmvanhouten.adventofcode2019.day10

import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Coordinate
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day10Test {

    val input = """
                        |#....#.....#...#.#.....#.#..#....#
                        |#..#..##...#......#.....#..###.#.#
                        |#......#.#.#.....##....#.#.....#..
                        |..#.#...#.......#.##..#...........
                        |.##..#...##......##.#.#...........
                        |.....#.#..##...#..##.....#...#.##.
                        |....#.##.##.#....###.#........####
                        |..#....#..####........##.........#
                        |..#...#......#.#..#..#.#.##......#
                        |.............#.#....##.......#...#
                        |.#.#..##.#.#.#.#.......#.....#....
                        |.....##.###..#.....#.#..###.....##
                        |.....#...#.#.#......#.#....##.....
                        |##.#.....#...#....#...#..#....#.#.
                        |..#.............###.#.##....#.#...
                        |..##.#.........#.##.####.........#
                        |##.#...###....#..#...###..##..#..#
                        |.........#.#.....#........#.......
                        |#.......#..#.#.#..##.....#.#.....#
                        |..#....#....#.#.##......#..#.###..
                        |......##.##.##...#...##.#...###...
                        |.#.....#...#........#....#.###....
                        |.#.#.#..#............#..........#.
                        |..##.....#....#....##..#.#.......#
                        |..##.....#.#......................
                        |.#..#...#....#.#.....#.........#..
                        |........#.............#.#.........
                        |#...#.#......#.##....#...#.#.#...#
                        |.#.....#.#.....#.....#.#.##......#
                        |..##....#.....#.....#....#.##..#..
                        |#..###.#.#....#......#...#........
                        |..#......#..#....##...#.#.#...#..#
                        |.#.##.#.#.....#..#..#........##...
                        |....#...##.##.##......#..#..##....
            """.trimMargin().parse()

    @Nested
    inner class Part1 {

        @Test
        internal fun `parse the input`() {
            val input = """.#.
                          |#.#
                          |##.
            """.trimMargin()
            assertThat(
                parse(input), equalTo(
                    setOf(
                        Coordinate(1, 0),
                        Coordinate(0, 1), Coordinate(2, 1),
                        Coordinate(0, 2), Coordinate(1, 2)
                    )
                )
            )
        }

        @Test
        internal fun `the best monitoring station is unblocked by others`() {
            val input = """#
                          |#
                          |#
            """.trimMargin().parse()

            assertThat(findMonitoringStation(input).location, equalTo(Coordinate(0, 1)))
        }

        @Test
        internal fun `asteroids block eachother horizontally`() {
            val input = "###".parse()

            assertThat(findMonitoringStation(input).location, equalTo(Coordinate(1, 0)))
        }

        @Test
        internal fun `asteroids block each other diagonally`() {
            val input = """#..
                          |.#.
                          |..#
            """.trimMargin().parse()

            assertThat(findMonitoringStation(input).location, equalTo(Coordinate(1, 1)))
        }

        @Test
        internal fun `3, 1 can view every other asteroid`() {
            val input = """#..#.
                          |.#.##
                          |..##.
            """.trimMargin().parse()

            assertThat(findMonitoringStation(input).location, equalTo(Coordinate(3, 1)))
        }

        @Test
        internal fun `this also blocks`() {
            val input = """#..
                          |...
                          |.#.
                          |...
                          |..#
            """.trimMargin().parse()

            assertThat(findMonitoringStation(input).location, equalTo(Coordinate(1, 2)))
        }

        @Test
        internal fun `and this also blocks`() {
            val input = """#..
                          |...
                          |...
                          |.#.
                          |...
                          |...
                          |..#
            """.trimMargin().parse()

            assertThat(findMonitoringStation(input).location, equalTo(Coordinate(1, 3)))
        }

        @Test
        internal fun `this blocks 2`() {
            val input = """#....
                          |..#..
                          |....#
            """.trimMargin().parse()

            assertThat(findMonitoringStation(input).location, equalTo(Coordinate(2, 1)))
        }

        @Test
        internal fun `this blocks 3`() {
            val input = """#......
                          |..#....
                          |.......
                          |......#
            """.trimMargin().parse()

            assertThat(findMonitoringStation(input).location, equalTo(Coordinate(2, 1)))
        }

        @Test
        internal fun `this blocks 4`() {
            val input = """.........#
                          |..........
                          |...#......
                          |#.........
            """.trimMargin().parse()

            assertThat(findMonitoringStation(input).location, equalTo(Coordinate(3, 2)))
        }

        @Test
        internal fun `testInput 0`() {
            val input = """.#..#
                          |.....
                          |#####
                          |....#
                          |...##
            """.trimMargin().parse()

            val (location, sees) = findMonitoringStation(input)
            assertThat(location, equalTo(Coordinate(3, 4)))
            assertThat(sees.size, equalTo(8))
        }

        @Test
        internal fun `testInput 1`() {
            val input = """......#.#.
                          |#..#.#....
                          |..#######.
                          |.#.#.###..
                          |.#..#.....
                          |..#....#.#
                          |#..#....#.
                          |.##.#..###
                          |##...#..#.
                          |.#....####
            """.trimMargin().parse()

            val (location, sees) = findMonitoringStation(input)
            println(location)
            println(sees.size)
            println(draw(sees))
            assertThat(location, equalTo(Coordinate(5, 8)))
            assertThat(sees.size, equalTo(33))
        }

        @Test
        internal fun `testInput 2`() {
            val input = """#.#...#.#.
                          |.###....#.
                          |.#....#...
                          |##.#.#.#.#
                          |....#.#.#.
                          |.##..###.#
                          |..#...##..
                          |..##....##
                          |......#...
                          |.####.###.
            """.trimMargin().parse()

            val (location, sees) = findMonitoringStation(input)
            println(location)
            println(sees.size)
            println(draw(sees))
            assertThat(location, equalTo(Coordinate(1, 2)))
            assertThat(sees.size, equalTo(35))
        }

        @Test
        internal fun `testInput 3`() {
            val input = """.#..#..###
                          |####.###.#
                          |....###.#.
                          |..###.##.#
                          |##.##.#.#.
                          |....###..#
                          |..#.#..#.#
                          |#..#.#.###
                          |.##...##.#
                          |.....#.#..
            """.trimMargin().parse()

            val (location, sees) = findMonitoringStation(input)
            println(location)
            println(sees.size)
            println(draw(sees))
            assertThat(location, equalTo(Coordinate(6, 3)))
            assertThat(sees.size, equalTo(41))
        }

        @Test
        internal fun `testInput 4`() {
            val input = """
                        |.#..##.###...#######
                        |##.############..##.
                        |.#.######.########.#
                        |.###.#######.####.#.
                        |#####.##.#.##.###.##
                        |..#####..#.#########
                        |####################
                        |#.####....###.#.#.##
                        |##.#################
                        |#####.##.###..####..
                        |..######..##.#######
                        |####.##.####...##..#
                        |.#####..#.######.###
                        |##...#.##########...
                        |#.##########.#######
                        |.####.#.###.###.#.##
                        |....##.##.###..#####
                        |.#.#.###########.###
                        |#.#.#.#####.####.###
                        |###.##.####.##.#..##
            """.trimMargin().parse()

            val (location, sees) = findMonitoringStation(input)
            println(location)
            println(sees.size)
            println(draw(sees))
            assertThat(location, equalTo(Coordinate(11, 13)))
            assertThat(sees.size, equalTo(210))
        }

        @Test
        internal fun `part 1`() {
            val (location, sees) = findMonitoringStation(input)
            println(location)
            println(sees.size)
            println(draw(sees))
            assertThat(sees.size, equalTo(267))
            assertThat(location, equalTo(Coordinate(26, 28)))
        }

    }

    private fun draw(coordinates: Collection<Coordinate>): String {
        return (0..coordinates.map { it.y }.max()!!).joinToString("\n") { y ->
            (0..coordinates.map { it.x }.max()!!).joinToString(""){ x ->
                if (coordinates.contains(Coordinate(x, y))) {
                    "#"
                } else {
                    "."
                }
            }
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `it destroys top then right then bottom then left`() {
            val input = """
                |.#.
                |#O#
                |.#.
            """.trimMargin().toStation()
            assertThat(
                sort(input.location, input.sees),
                equalTo(
                    listOf(
                        Coordinate(x = 1, y = 0),
                        Coordinate(x = 2, y = 1),
                        Coordinate(x = 1, y = 2),
                        Coordinate(x = 0, y = 1)
                    )
                )
            )

        }

        @Test
        internal fun `it handles non astute angles`() {
            val input = """
                |...#..
                |.#..##
                |.O#...
                |......
            """.trimMargin().toStation()
            assertThat(
                sort(input.location, input.sees),
                equalTo(
                    listOf(
                        Coordinate(x = 1, y = 1),
                        Coordinate(x = 3, y = 0),
                        Coordinate(x = 4, y = 1),
                        Coordinate(x = 5, y = 1),
                        Coordinate(x = 2, y = 2)
                    )
                )
            )
        }

        @Test
        internal fun `it handles non angles over 90`() {
            val input = """
                |......
                |......
                |.O#...
                |.....#
                |..#...
                |....##
            """.trimMargin().toStation()
            assertThat(
                sort(input.location, input.sees),
                equalTo(
                    listOf(
                        Coordinate(x = 2, y = 2),
                        Coordinate(x = 5, y = 3),
                        Coordinate(x = 5, y = 5),
                        Coordinate(x = 4, y = 5),
                        Coordinate(x = 2, y = 4)
                    )
                )
            )
        }

        @Test
        internal fun `it handles non angles over 180`() {
            val input = """
                |.........
                |.........
                |....O....
                |#...#....
                |#........
                |..##.....
            """.trimMargin().toStation()
            assertThat(
                sort(input.location, input.sees),
                equalTo(
                    listOf(
                        Coordinate(x = 4, y = 3),
                        Coordinate(x = 3, y = 5),
                        Coordinate(x = 2, y = 5),
                        Coordinate(x = 0, y = 4),
                        Coordinate(x = 0, y = 3)
                    )
                )
            )
        }

        @Test
        internal fun `it handles non angles over 270`() {
            val input = """
                |...#.....
                |..#......
                |.##......
                |.#..O....
                |.........
                |.........
                |.........
            """.trimMargin().toStation()
            assertThat(
                sort(input.location, input.sees),
                equalTo(
                    listOf(
                        Coordinate(x = 1, y = 3),
                        Coordinate(x = 1, y = 2),
                        Coordinate(x = 2, y = 2),
                        Coordinate(x = 2, y = 1),
                        Coordinate(x = 3, y = 0)
                    )
                )
            )
        }

        @Test
        internal fun part2() {
            val (location, sees) = findMonitoringStation(input)
            assertThat(sort(location, sees)[199], equalTo(Coordinate(13, 9)))
        }
    }

}

private fun String.toStation(): Station {
    return Station(locateStation(this), this.parse())
}

fun locateStation(rawMap: String): Coordinate {
    val (y, row) = rawMap.split('\n')
        .mapIndexed { i, row -> i to row }
        .first { (_, row) -> row.contains('O') }
    val x = row.mapIndexed { i, c -> i to c }
        .find { it.second == 'O' }?.first ?: throw IllegalStateException("No 'O' in the map")
    return Coordinate(x, y)


}

private fun String.parse(): Set<Coordinate> {
    return parse(this)
}
