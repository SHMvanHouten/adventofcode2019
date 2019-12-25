package com.github.shmvanhouten.adventofcode2019.day22

import com.github.shmvanhouten.adventofcode2019.day22.Instructiontype.*
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day22Test {

    @Nested
    inner class Parse {

        @Test
        internal fun `it parses the instructions`() {
            val input = """
                |deal with increment 12
                |cut 123
                |cut -456
                |deal into new stack
            """.trimMargin()
            assertThat(
                parseInstructions(input), equalTo(
                    listOf(
                        ShuffleInstruction(DEAL_WITH_INCREMENT, 12),
                        ShuffleInstruction(CUT, 123),
                        ShuffleInstruction(CUT, -456),
                        ShuffleInstruction(DEAL_INTO_NEW_STACK)
                    )
                )
            )
        }
    }

    @Nested
    inner class Part1 {

        @Test
        internal fun `no shuffle instruction gives back the deck`() {
            assertThat(shuffle(10, emptyList()), equalTo(listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)))
        }

        @Test
        internal fun `deal into new stack reverses the input`() {
            val instructions = listOf(
                ShuffleInstruction(DEAL_INTO_NEW_STACK)
            )
            assertThat(shuffle(10, instructions), equalTo(listOf(9, 8, 7, 6, 5, 4, 3, 2, 1, 0)))
        }

        @Test
        internal fun `cut n cards positive input`() {
            assertThat(
                shuffle(10, listOf(ShuffleInstruction(CUT, 3))),
                equalTo(listOf(3, 4, 5, 6, 7, 8, 9, 0, 1, 2))
            )
            assertThat(
                shuffle(10, listOf(ShuffleInstruction(CUT, 4))),
                equalTo(listOf(4, 5, 6, 7, 8, 9, 0, 1, 2, 3))
            )
        }

        @Test
        internal fun `cut n cards negative input`() {
            assertThat(
                shuffle(10, listOf(ShuffleInstruction(CUT, -4))),
                equalTo(listOf(6, 7, 8, 9, 0, 1, 2, 3, 4, 5))
            )
            assertThat(
                shuffle(10, listOf(ShuffleInstruction(CUT, -5))),
                equalTo(shuffle(10, listOf(ShuffleInstruction(CUT, 5))))
            )
        }

        @Test
        internal fun `deal with increment`() {
            assertThat(
                shuffle(10, listOf(ShuffleInstruction(DEAL_WITH_INCREMENT, 3))),
                equalTo(listOf(0, 7, 4, 1, 8, 5, 2, 9, 6, 3))
            )
            assertThat(
                shuffle(10, listOf(ShuffleInstruction(DEAL_WITH_INCREMENT, 7))),
                equalTo(listOf(0, 3, 6, 9, 2, 5, 8, 1, 4, 7))
            )
        }

        @Test
        internal fun `part 1`() {
            val shuffledDeck = shuffle(10007, parseInstructions(input))
            assertThat(
                findPositionOfCard(2019, shuffledDeck),
                equalTo(7395)
            )
        }
    }

    private val input = """deal with increment 18
cut -3893
deal with increment 15
cut -3085
deal with increment 43
cut -2092
deal into new stack
cut 7372
deal with increment 66
deal into new stack
cut -5126
deal with increment 60
cut 2307
deal with increment 5
cut 971
deal with increment 74
cut -3236
deal with increment 29
cut -6691
deal with increment 64
cut -8296
deal with increment 49
cut -1717
deal with increment 55
deal into new stack
cut 2992
deal with increment 65
cut 2166
deal with increment 72
cut 4752
deal with increment 35
cut 8476
deal with increment 50
cut -6138
deal with increment 73
cut -91
deal with increment 73
cut 2012
deal with increment 4
cut 3963
deal into new stack
cut 1186
deal with increment 25
cut 8476
deal with increment 36
cut -6069
deal with increment 18
deal into new stack
deal with increment 56
cut -6009
deal with increment 33
cut 1273
deal with increment 10
cut 6912
deal with increment 62
deal into new stack
deal with increment 48
cut -9706
deal with increment 53
cut 6162
deal with increment 38
cut 6576
deal with increment 10
cut 9123
deal with increment 4
cut 1355
deal with increment 34
cut -3784
deal with increment 59
deal into new stack
cut -9109
deal with increment 3
cut 4903
deal with increment 73
cut 8575
deal with increment 34
deal into new stack
cut -5046
deal with increment 75
deal into new stack
deal with increment 42
cut 4671
deal with increment 57
deal into new stack
deal with increment 14
cut 5464
deal with increment 37
cut 6782
deal with increment 29
cut 4233
deal with increment 37
cut -5577
deal with increment 50
cut -3111
deal with increment 56
deal into new stack
deal with increment 75
cut 1205
deal with increment 2
cut -7531"""
}
