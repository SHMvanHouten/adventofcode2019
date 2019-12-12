package com.github.shmvanhouten.adventofcode2019.day11

import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Coordinate
import com.github.shmvanhouten.adventofcode2019.day02.Computer
import com.github.shmvanhouten.adventofcode2019.day02.ExecutionType
import com.github.shmvanhouten.adventofcode2019.day02.ExecutionType.RUN
import com.github.shmvanhouten.adventofcode2019.day02.IComputer
import com.github.shmvanhouten.adventofcode2019.day02.IntCode
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*

class Day11Test {

//    val input = "3,8,1005,8,325,1106,0,11,0,0,0,104,1,104,0,3,8,102,-1,8,10,1001,10,1,10,4,10,108,0,8,10,4,10,101,0,8,28,2,3,7,10,2,1109,3,10,2,102,0,10,2,1005,12,10,3,8,102,-1,8,10,101,1,10,10,4,10,1008,8,0,10,4,10,101,0,8,67,2,109,12,10,1,1003,15,10,3,8,1002,8,-1,10,1001,10,1,10,4,10,108,1,8,10,4,10,101,0,8,96,3,8,102,-1,8,10,101,1,10,10,4,10,1008,8,0,10,4,10,1002,8,1,119,3,8,102,-1,8,10,1001,10,1,10,4,10,1008,8,0,10,4,10,101,0,8,141,3,8,1002,8,-1,10,101,1,10,10,4,10,108,0,8,10,4,10,1001,8,0,162,1,106,17,10,1006,0,52,1006,0,73,3,8,102,-1,8,10,1001,10,1,10,4,10,108,1,8,10,4,10,1001,8,0,194,1006,0,97,1,1004,6,10,1006,0,32,2,8,20,10,3,8,102,-1,8,10,101,1,10,10,4,10,1008,8,1,10,4,10,102,1,8,231,1,1,15,10,1006,0,21,1,6,17,10,2,1005,8,10,3,8,102,-1,8,10,101,1,10,10,4,10,108,1,8,10,4,10,102,1,8,267,2,1007,10,10,3,8,1002,8,-1,10,1001,10,1,10,4,10,1008,8,1,10,4,10,102,1,8,294,1006,0,74,2,1003,2,10,1,107,1,10,101,1,9,9,1007,9,1042,10,1005,10,15,99,109,647,104,0,104,1,21101,936333018008,0,1,21101,342,0,0,1106,0,446,21102,937121129228,1,1,21101,0,353,0,1105,1,446,3,10,104,0,104,1,3,10,104,0,104,0,3,10,104,0,104,1,3,10,104,0,104,1,3,10,104,0,104,0,3,10,104,0,104,1,21101,0,209383001255,1,21102,400,1,0,1106,0,446,21101,0,28994371675,1,21101,411,0,0,1105,1,446,3,10,104,0,104,0,3,10,104,0,104,0,21101,867961824000,0,1,21101,0,434,0,1106,0,446,21102,1,983925674344,1,21101,0,445,0,1106,0,446,99,109,2,21201,-1,0,1,21102,40,1,2,21101,477,0,3,21102,467,1,0,1106,0,510,109,-2,2106,0,0,0,1,0,0,1,109,2,3,10,204,-1,1001,472,473,488,4,0,1001,472,1,472,108,4,472,10,1006,10,504,1101,0,0,472,109,-2,2106,0,0,0,109,4,1201,-1,0,509,1207,-3,0,10,1006,10,527,21102,1,0,-3,21202,-3,1,1,21201,-2,0,2,21102,1,1,3,21102,1,546,0,1106,0,551,109,-4,2105,1,0,109,5,1207,-3,1,10,1006,10,574,2207,-4,-2,10,1006,10,574,22101,0,-4,-4,1105,1,642,21202,-4,1,1,21201,-3,-1,2,21202,-2,2,3,21101,0,593,0,1105,1,551,22102,1,1,-4,21101,1,0,-1,2207,-4,-2,10,1006,10,612,21102,1,0,-1,22202,-2,-1,-2,2107,0,-3,10,1006,10,634,21201,-1,0,1,21101,634,0,0,105,1,509,21202,-2,-1,-2,22201,-4,-2,-4,109,-5,2106,0,0".split(',').map { it.toLong() }

    val input = "3,8,1005,8,319,1106,0,11,0,0,0,104,1,104,0,3,8,1002,8,-1,10,101,1,10,10,4,10,108,0,8,10,4,10,1002,8,1,28,3,8,1002,8,-1,10,1001,10,1,10,4,10,1008,8,1,10,4,10,102,1,8,51,2,1008,18,10,3,8,1002,8,-1,10,1001,10,1,10,4,10,1008,8,1,10,4,10,101,0,8,77,1,1006,8,10,1006,0,88,3,8,1002,8,-1,10,1001,10,1,10,4,10,1008,8,1,10,4,10,1002,8,1,106,1006,0,47,2,5,0,10,3,8,102,-1,8,10,101,1,10,10,4,10,1008,8,0,10,4,10,101,0,8,135,2,105,3,10,2,1101,6,10,3,8,102,-1,8,10,101,1,10,10,4,10,1008,8,0,10,4,10,1002,8,1,165,3,8,102,-1,8,10,101,1,10,10,4,10,108,0,8,10,4,10,1002,8,1,186,1,1009,11,10,1,9,3,10,2,1003,10,10,1,107,11,10,3,8,1002,8,-1,10,101,1,10,10,4,10,1008,8,1,10,4,10,1002,8,1,225,1006,0,25,1,1009,14,10,1,1008,3,10,3,8,102,-1,8,10,101,1,10,10,4,10,108,1,8,10,4,10,1002,8,1,257,1,1006,2,10,3,8,1002,8,-1,10,1001,10,1,10,4,10,1008,8,0,10,4,10,101,0,8,284,2,1004,7,10,1006,0,41,2,1106,17,10,1,104,3,10,101,1,9,9,1007,9,919,10,1005,10,15,99,109,641,104,0,104,1,21101,0,937108545948,1,21102,1,336,0,1105,1,440,21102,1,386577203612,1,21102,347,1,0,1105,1,440,3,10,104,0,104,1,3,10,104,0,104,0,3,10,104,0,104,1,3,10,104,0,104,1,3,10,104,0,104,0,3,10,104,0,104,1,21102,1,21478178819,1,21102,1,394,0,1106,0,440,21102,21477985447,1,1,21101,405,0,0,1105,1,440,3,10,104,0,104,0,3,10,104,0,104,0,21101,984458351460,0,1,21101,428,0,0,1106,0,440,21101,709048034148,0,1,21102,439,1,0,1106,0,440,99,109,2,21201,-1,0,1,21101,0,40,2,21101,471,0,3,21102,461,1,0,1105,1,504,109,-2,2106,0,0,0,1,0,0,1,109,2,3,10,204,-1,1001,466,467,482,4,0,1001,466,1,466,108,4,466,10,1006,10,498,1101,0,0,466,109,-2,2105,1,0,0,109,4,2101,0,-1,503,1207,-3,0,10,1006,10,521,21101,0,0,-3,22102,1,-3,1,21201,-2,0,2,21102,1,1,3,21102,540,1,0,1106,0,545,109,-4,2105,1,0,109,5,1207,-3,1,10,1006,10,568,2207,-4,-2,10,1006,10,568,22101,0,-4,-4,1105,1,636,21201,-4,0,1,21201,-3,-1,2,21202,-2,2,3,21102,587,1,0,1106,0,545,21202,1,1,-4,21102,1,1,-1,2207,-4,-2,10,1006,10,606,21101,0,0,-1,22202,-2,-1,-2,2107,0,-3,10,1006,10,628,22101,0,-1,1,21101,628,0,0,105,1,503,21202,-2,-1,-2,22201,-4,-2,-4,109,-5,2105,1,0".split(',').map { it.toLong() }
    @Nested
    inner class Part1 {

        private lateinit var computer: FakeComputer

        @BeforeEach
        internal fun setUp() {
            computer = FakeComputer()
        }

        @Test
        internal fun `robot tells computer it is on a white square`() {
            val alan = AlanBot(computer)

            computer.output.add(0) // to prevent null pointer
            computer.output.add(0) // to prevent null pointer

            alan.tick()
            assertThat(computer.inputs.poll(), equalTo(1L))
        }

        @Test
        internal fun `the computer tells the bot to paint the panel black`() {
            val alan = AlanBot(computer)

            computer.output.add(0) // paint black
            computer.output.add(0) // to prevent null pointer
            alan.tick()

            assertThat(alan.locationPainted, equalTo(Coordinate(0, 0)))
        }

        @Test
        internal fun `the computer tells the bot to paint the panel white`() {
            val alan = AlanBot(computer)

            computer.output.add(1) // paint white
            computer.output.add(0) // to prevent null pointer
            alan.tick()

            assertTrue(alan.locationPainted == null)
        }

        @Test
        internal fun `after the paint command the computer tells the panel to turn left`() {
            // 0 means it should turn left 90 degrees
            val alan = AlanBot(computer)

            computer.output.add(1) // paint white
            computer.output.add(0) // turn left

            alan.tick()
            computer.output.add(0) // paint black
            computer.output.add(1) // to prevent null pointer
            alan.tick()

            assertThat(alan.locationPainted, equalTo(Coordinate(-1, 0)))
        }

        @Test
        internal fun `after the paint command the computer tells the panel to turn right`() {
            // 0 means it should turn left 90 degrees
            val alan = AlanBot(computer)

            computer.output.add(1) // paint white
            computer.output.add(1) // turn left

            alan.tick()
            computer.output.add(0) // paint black
            computer.output.add(1) // to prevent null pointer
            alan.tick()

            assertThat(alan.locationPainted, equalTo(Coordinate(1, 0)))
        }

        @Test
        internal fun `robot tells the computer it is over a black square after a bunch of moves`() {
            val alan = AlanBot(computer)

            computer.output.add(0) // paint black
            computer.output.add(0) // turn left
            alan.tick()
            computer.output.add(1) // paint white
            computer.output.add(0) // turn left
            alan.tick()
            computer.output.add(1) // paint white
            computer.output.add(0) // turn left
            alan.tick()
            computer.output.add(1) // paint white
            computer.output.add(0) // turn left
            alan.tick()
            computer.output.add(1) // paint white
            computer.output.add(0) // turn left
            alan.tick()

            assertThat(computer.inputs.last(), equalTo(0L))
        }

        @Test
        internal fun part1() {
            val computer = Computer(IntCode(input))
            val alan = AlanBot(computer)
            var isRunning = true
            val panels = mutableSetOf<Coordinate>()
            var painted = 0
            while (isRunning) {
                painted++
                isRunning = alan.tick()
                val lastPanelAdded = alan.locationPainted
                panels += lastPanelAdded

            }
            println(draw(alan.panels))
            println(panels.size)
            println(alan.panels.size)

            // 248 too low
            println(painted)
        }
    }

}

class FakeComputer : IComputer {
    override val output: Queue<Long> = LinkedList<Long>()

    val inputs = LinkedList<Long>()

    var executionType = RUN

    override fun run(input: Long): ExecutionType {
        inputs += input
        return executionType
    }
}

private fun draw(coordinates: Collection<Coordinate>): String {
    return (0..coordinates.map { it.y }.max()!!).joinToString("\n") { y ->
        (0..coordinates.map { it.x }.max()!!).joinToString("") { x ->
            if (coordinates.contains(Coordinate(x, y))) {
                '\u2591'.toString()
            } else {
                '\u2588'.toString()
            }
        }
    }
}
