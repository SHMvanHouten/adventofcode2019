package com.github.shmvanhouten.adventofcode2019.day02

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.RuntimeException

class IntCodesTest {

    val inputPt1 =
        "1,12,2,3,1,1,2,3,1,3,4,3,1,5,0,3,2,10,1,19,2,9,19,23,2,13,23,27,1,6,27,31,2,6,31,35,2,13,35,39,1,39,10,43,2,43,13,47,1,9,47,51,1,51,13,55,1,55,13,59,2,59,13,63,1,63,6,67,2,6,67,71,1,5,71,75,2,6,75,79,1,5,79,83,2,83,6,87,1,5,87,91,1,6,91,95,2,95,6,99,1,5,99,103,1,6,103,107,1,107,2,111,1,111,5,0,99,2,14,0,0"
    val inputPt2 =
        "1,0,0,3,1,1,2,3,1,3,4,3,1,5,0,3,2,10,1,19,2,9,19,23,2,13,23,27,1,6,27,31,2,6,31,35,2,13,35,39,1,39,10,43,2,43,13,47,1,9,47,51,1,51,13,55,1,55,13,59,2,59,13,63,1,63,6,67,2,6,67,71,1,5,71,75,2,6,75,79,1,5,79,83,2,83,6,87,1,5,87,91,1,6,91,95,2,95,6,99,1,5,99,103,1,6,103,107,1,107,2,111,1,111,5,0,99,2,14,0,0"

    @Nested
    inner class Part1 {

        @Test
        internal fun `1, 0, 0, 0 becomes 2, 0, 0, 0`() {
            val intCode = IntCode(1, 0, 0, 0, 99)
            assertThat(Computer(intCode = intCode).run(), equalTo(listOf(2L, 0L, 0L, 0L, 99)))
        }

        @Test
        internal fun `1, 1, 1, 0 becomes 2, 1, 1, 0`() {
            val intCode = IntCode(1, 1, 1, 0, 99)
            assertThat(Computer(intCode = intCode).run(), equalTo(listOf(2L, 1L, 1L, 0L, 99)))
        }

        @Test
        internal fun `1, 0, 0, 1 becomes 1, 2, 0, 1`() {
            // intCode[3] determines the position of the new value
            val intCode = IntCode(1, 0, 0, 1, 99)
            assertThat(Computer(intCode = intCode).run(), equalTo(listOf(1L, 2L, 0L, 1L, 99)))
        }

        @Test
        internal fun `1, 2, 1, 1 becomes 1, 4, 1, 1`() {
            val intCode = IntCode(1, 2, 1, 1, 99)
            assertThat(Computer(intCode = intCode).run(), equalTo(listOf(1L, 3L, 1L, 1L, 99)))
        }

        @Test
        internal fun `1,0,0,0,99 becomes 2,0,0,0,99`() {
            // one of the examples
            val intCode = IntCode(1, 0, 0, 0, 99, 99)
            assertThat(Computer(intCode = intCode).run(), equalTo(listOf(2L, 0L, 0L, 0L, 99, 99)))
        }

        @Test
        internal fun `2,0,0,1 becomes 2, 4, 0, 1`() {
            val intCode = IntCode(2, 0, 0, 1, 99)
            assertThat(Computer(intCode = intCode).run(), equalTo(listOf(2L, 4L, 0L, 1L, 99)))
        }

        //2,3,0,3,99 becomes 2,3,0,6,99 (3 * 2 = 6).
        @Test
        internal fun `2,3,0,3,99 becomes 2,3,0,6,99`() {
            val intCode = IntCode(2, 4, 4, 5, 99, 0)
            assertThat(Computer(intCode = intCode).run(), equalTo(listOf(2L, 4L, 4L, 5L, 99, 9801)))
        }

        //2,4,4,5,99,0 becomes 2,4,4,5,99,9801 (99 * 99 = 9801).
        @Test
        internal fun `2,4,4,5,99,0 becomes 2,4,4,5,99,9801`() {
            val intCode = IntCode(2, 4, 4, 5, 99, 0)
            assertThat(Computer(intCode = intCode).run(), equalTo(listOf(2L, 4L, 4L, 5L, 99, 9801)))
        }

        @Test
        internal fun `99 stops the program`() {
            val intCode = IntCode(99, 0, 0, 0)
            assertThat(Computer(intCode = intCode).run(), equalTo(listOf(99, 0L, 0L, 0L)))
        }

        @Test
        internal fun `an opcode other than 1, 2 or 99 crashes the program`() {
            assertThrows<RuntimeException> {
                Computer(intCode = IntCode(3)).run()
            }
        }

        @Test
        internal fun `we run the next block when this block is finished`() {
            assertThat(
                Computer(intCode = IntCode(1L, 0L, 0L, 1L, 1L, 0L, 0L, 1L, 99)).run(),
                equalTo(listOf(1L, 2L, 0L, 1L, 1L, 0L, 0L, 1L, 99))
            )
            assertThat(
                Computer(intCode = IntCode(1L, 0L, 0L, 1L, 1L, 0L, 0L, 4L, 99)).run(),
                equalTo(listOf(1L, 2L, 0L, 1L, 2L, 0L, 0L, 4L, 99))
            )
        }

        //1,1,1,4,99,5,6,0,99 becomes 30,1,1,4,2,5,6,0,99.
        @Test
        internal fun `1,1,1,4,99,5,6,0,99 becomes 30,1,1,4,2,5,6,0,99`() {
            val intCode = IntCode(1,1,1,4,99,5,6,0,99)
            assertThat(Computer(intCode = intCode).run(), equalTo(listOf(30L,1L,1L,4L,2L,5L,6L,0L,99)))
        }

        @Test
        internal fun part1() {
            val input = parseInput(inputPt1)
            println(Computer(intCode = IntCode(input)).run())
            // 2890696
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `part 2`() {
            println(findTargetOutput(IntCode(parseInput(inputPt2)), 19690720))
        }
    }

    private fun parseInput(input: String) = input.split(',').map { it.toInt().toLong() }

}
