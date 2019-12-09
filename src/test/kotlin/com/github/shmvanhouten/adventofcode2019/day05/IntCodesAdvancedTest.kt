package com.github.shmvanhouten.adventofcode2019.day05

import com.github.shmvanhouten.adventofcode2019.day02.Computer
import com.github.shmvanhouten.adventofcode2019.day02.IntCode
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class IntCodesAdvancedTest {

    val input =
        "3,225,1,225,6,6,1100,1,238,225,104,0,1101,32,43,225,101,68,192,224,1001,224,-160,224,4,224,102,8,223,223,1001,224,2,224,1,223,224,223,1001,118,77,224,1001,224,-87,224,4,224,102,8,223,223,1001,224,6,224,1,223,224,223,1102,5,19,225,1102,74,50,224,101,-3700,224,224,4,224,1002,223,8,223,1001,224,1,224,1,223,224,223,1102,89,18,225,1002,14,72,224,1001,224,-3096,224,4,224,102,8,223,223,101,5,224,224,1,223,224,223,1101,34,53,225,1102,54,10,225,1,113,61,224,101,-39,224,224,4,224,102,8,223,223,101,2,224,224,1,223,224,223,1101,31,61,224,101,-92,224,224,4,224,102,8,223,223,1001,224,4,224,1,223,224,223,1102,75,18,225,102,48,87,224,101,-4272,224,224,4,224,102,8,223,223,1001,224,7,224,1,224,223,223,1101,23,92,225,2,165,218,224,101,-3675,224,224,4,224,1002,223,8,223,101,1,224,224,1,223,224,223,1102,8,49,225,4,223,99,0,0,0,677,0,0,0,0,0,0,0,0,0,0,0,1105,0,99999,1105,227,247,1105,1,99999,1005,227,99999,1005,0,256,1105,1,99999,1106,227,99999,1106,0,265,1105,1,99999,1006,0,99999,1006,227,274,1105,1,99999,1105,1,280,1105,1,99999,1,225,225,225,1101,294,0,0,105,1,0,1105,1,99999,1106,0,300,1105,1,99999,1,225,225,225,1101,314,0,0,106,0,0,1105,1,99999,1107,226,226,224,1002,223,2,223,1005,224,329,1001,223,1,223,1007,677,226,224,1002,223,2,223,1006,224,344,1001,223,1,223,108,677,226,224,102,2,223,223,1006,224,359,1001,223,1,223,7,226,226,224,1002,223,2,223,1005,224,374,101,1,223,223,107,677,677,224,1002,223,2,223,1006,224,389,1001,223,1,223,1007,677,677,224,1002,223,2,223,1006,224,404,1001,223,1,223,1107,677,226,224,1002,223,2,223,1005,224,419,1001,223,1,223,108,226,226,224,102,2,223,223,1006,224,434,1001,223,1,223,1108,226,677,224,1002,223,2,223,1006,224,449,1001,223,1,223,1108,677,226,224,102,2,223,223,1005,224,464,1001,223,1,223,107,226,226,224,102,2,223,223,1006,224,479,1001,223,1,223,1008,226,226,224,102,2,223,223,1005,224,494,101,1,223,223,7,677,226,224,1002,223,2,223,1005,224,509,101,1,223,223,8,226,677,224,1002,223,2,223,1006,224,524,1001,223,1,223,1007,226,226,224,1002,223,2,223,1006,224,539,101,1,223,223,1008,677,677,224,1002,223,2,223,1006,224,554,101,1,223,223,1108,677,677,224,102,2,223,223,1006,224,569,101,1,223,223,1107,226,677,224,102,2,223,223,1005,224,584,1001,223,1,223,8,677,226,224,1002,223,2,223,1006,224,599,101,1,223,223,1008,677,226,224,102,2,223,223,1006,224,614,1001,223,1,223,7,226,677,224,1002,223,2,223,1005,224,629,101,1,223,223,107,226,677,224,102,2,223,223,1005,224,644,101,1,223,223,8,677,677,224,102,2,223,223,1005,224,659,1001,223,1,223,108,677,677,224,1002,223,2,223,1005,224,674,101,1,223,223,4,223,99,226"
            .split(',').map { it.toInt().toLong() }

    @Nested
    inner class Part1 {

        @Test
        internal fun `instruction 3 reads the input, 4 outputs the input`() {
            val computer = Computer(IntCode(3, 0, 4, 0, 99))
            computer.run(100)
            assertThat(computer.output, equalTo(listOf(100L)))
        }

        @Test
        internal fun `we should support parameter modes`() {
            val intCode = IntCode(1002, 4, 3, 4, 33)
            val computer = Computer(intCode = intCode)
            computer.run()
            assertThat(computer.getIntCodes(), equalTo(listOf(1002L, 4L, 3L, 4L, 99)))
        }

        @Test
        internal fun part1() {
            val computer = Computer( IntCode(input))
            computer.run(1)
            println(computer.output)
            assertThat(computer.output.last(), equalTo(5821753L))
        }
    }

    @Nested
    inner class Part2 {

        @Nested
        inner class `We support less than instruction` {

            @Test
            internal fun `1 is less than 7`() {
                // intcode[1] (= 1) < intcode[0] (=7) is true therefore intcode[0] is set to 1
                val intCode = IntCode(7, 1, 0, 0, 99)
                val computer = Computer(intCode = intCode)
                computer.run()
                assertThat(computer.getIntCodes(), equalTo(listOf(1L, 1L, 0L, 0L, 99)))
            }

            @Test
            internal fun `7 is NOT less than 2`() {
                // intcode[1] (= 7) < intcode[2] (=2) is false therefore intcode[0] is set to 0
                val intCode = IntCode(7, 0, 2, 0, 99)
                val computer = Computer(intCode = intCode)
                computer.run()
                assertThat(computer.getIntCodes(), equalTo(listOf(0L, 0L, 2L, 0L, 99)))
            }

            @Test
            internal fun `less than supports parameter mode`() {
                // 3 < 2 is false therefore intcode[0] is set to 0
                val intCode = IntCode(1107, 3, 2, 0, 99)
                val computer = Computer(intCode = intCode)
                computer.run()
                assertThat(computer.getIntCodes(), equalTo(listOf(0L, 3L, 2L, 0L, 99)))
            }
        }

        @Nested
        inner class `We support equals instruction` {
            @Test
            internal fun `7 is NOT equal to 1`() {
                // intcode[1] (= 1) == intcode[0] (=7) is false therefore intcode[0] is set to 0
                val intCode = IntCode(8, 1, 0, 0, 99)
                val computer = Computer(intCode = intCode)
                computer.run()
                assertThat(computer.getIntCodes(), equalTo(listOf(0L, 1L, 0L, 0L, 99)))
            }

            @Test
            internal fun `equals supports paramater mode`() {
                val intCode = IntCode(1108, 1, 3, 1, 99)
                val computer = Computer(intCode = intCode)
                computer.run()
                assertThat(computer.getIntCodes(), equalTo(listOf(1108L, 0L, 3L, 1L, 99)))
            }

            @Test
            internal fun `some test values`() {
                val intCode = IntCode(3L, 9L, 8L, 9L, 10L, 9L, 4L, 9L, 99L, -1L, 8)
                val computer1 = Computer(intCode)
                computer1.run(8)
                assertThat(computer1.output.first(), equalTo(1L))
                val computer2 = Computer(intCode)
                computer2.run(7)
                assertThat(computer2.output.first(), equalTo(0L))
            }
        }

        @Test
        internal fun `we support jump to if true`() {
            //Here are some jump tests that take an input, then output 0 if the input was zero or 1 if the input was non-zero
            val intCode = IntCode(3, 12, 6, 12, 15, 1, 13, 14, 13, 4, 13, 99, -1, 0, 1, 9)
            val computer1 = Computer(intCode)
            computer1.run(0)
            assertThat(computer1.output.first(), equalTo(0L))
            val computer2 = Computer(intCode)
            computer2.run(99)
            assertThat(computer2.output.first(), equalTo(1L))

        }

        @Test
        internal fun `test input`() {
            /*
            The example program uses an input instruction to ask for a single number.
            The program will then output 999 if the input value is below 8,
            output 1000 if the input value is equal to 8,
            or output 1001 if the input value is greater than 8.
             */
            val intCode = IntCode(
                3, 21, 1008, 21, 8, 20, 1005, 20, 22, 107, 8, 21, 20, 1006, 20, 31,
                1106, 0, 36, 98, 0, 0, 1002, 21, 125, 20, 4, 20, 1105, 1, 46, 104,
                999, 1105, 1, 46, 1101, 1000, 1, 20, 4, 20, 1105, 1, 46, 98, 99
            )
            assertIntCode(intCode, 7, 999)
            assertIntCode(intCode, 8, 1000)
            assertIntCode(intCode, 9, 1001)

        }

        @Test
        internal fun `part 2`() {
            val computer = Computer(IntCode(input))
            computer.run(5)
            println(computer.output)
            assertThat(computer.output.first(), equalTo(11956381L))
        }

        private fun assertIntCode(
            intCode: IntCode,
            input: Long,
            expected: Long
        ) {
            val computer = Computer(intCode)
            computer.run(input)
            assertThat(computer.output.first(), equalTo(expected))
        }
    }

}
