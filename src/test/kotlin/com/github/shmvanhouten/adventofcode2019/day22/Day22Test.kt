package com.github.shmvanhouten.adventofcode2019.day22

import com.github.shmvanhouten.adventofcode2019.day22.Instructiontype.*
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.math.BigInteger

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
                        ShuffleInstruction(DEAL_WITH_INCREMENT, BigInteger.valueOf(12)),
                        ShuffleInstruction(CUT, BigInteger.valueOf(123)),
                        ShuffleInstruction(CUT, BigInteger.valueOf(-456)),
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
            assertThat(
                shuffleToList(10, emptyList()), equalTo(listOf(0L, 1, 2, 3, 4, 5, 6, 7, 8, 9))
            )
        }

        @Test
        internal fun `deal into new stack reverses the input`() {
            val instructions = listOf(
                ShuffleInstruction(DEAL_INTO_NEW_STACK)
            )
            assertThat(
                shuffleToList(10, instructions), equalTo(listOf(9L, 8, 7, 6, 5, 4, 3, 2, 1, 0))
            )
            assertThat(
                shuffleToList(10007, instructions), equalTo(0L.until(10007L).toList().reversed())
            )
        }

        @Test
        internal fun `cut n cards positive input`() {
            assertThat(
                shuffleToList(10, listOf(ShuffleInstruction(CUT, BigInteger.valueOf(3)))),
                equalTo(listOf(3L, 4, 5, 6, 7, 8, 9, 0, 1, 2))
            )
            assertThat(
                shuffleToList(10, listOf(ShuffleInstruction(CUT, BigInteger.valueOf(4)))),
                equalTo(listOf(4L, 5, 6, 7, 8, 9, 0, 1, 2, 3))
            )
            assertThat(
                shuffleToList(10, listOf(ShuffleInstruction(CUT, BigInteger.valueOf(8)))),
                equalTo(listOf(8L, 9, 0, 1, 2, 3, 4, 5, 6, 7))
            )
        }

        @Test
        internal fun `cut n cards negative input`() {
            assertThat(
                shuffleToList(10, listOf(ShuffleInstruction(CUT, BigInteger.valueOf(-4)))),
                equalTo(listOf(6L, 7, 8, 9, 0, 1, 2, 3, 4, 5))
            )
            assertThat(
                shuffleToList(10, listOf(ShuffleInstruction(CUT, BigInteger.valueOf(-5)))),
                equalTo(shuffleToList(10, listOf(ShuffleInstruction(CUT, BigInteger.valueOf(5)))))
            )
            assertThat(
                shuffleToList(10, listOf(ShuffleInstruction(CUT, BigInteger.valueOf(-9)))),
                equalTo(shuffleToList(10, listOf(ShuffleInstruction(CUT, BigInteger.valueOf(1)))))
            )
        }

        @Test
        internal fun `deal with increment`() {
            assertThat(
                shuffleToList(10L, listOf(ShuffleInstruction(DEAL_WITH_INCREMENT, BigInteger.valueOf(3)))),
                equalTo(listOf(0L, 7, 4, 1, 8, 5, 2, 9, 6, 3))
            )
            assertThat(
                shuffleToList(10L, listOf(ShuffleInstruction(DEAL_WITH_INCREMENT, BigInteger.valueOf(7)))),
                equalTo(listOf(0L, 3, 6, 9, 2, 5, 8, 1, 4, 7))
            )
        }

        @Test
        internal fun `part 1`() {
            val shuffledDeck = shuffle(10007, parseInstructions(input))
            assertThat(
                findPositionOfCard(2019L, shuffledDeck),
                equalTo(7395L)
            )
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `a deal with increment on card 2019 on a deck with 10007 cards`() {
            // card 2019 becomes 2019 * 18 % 10007
            assertThat(
                dealWithIncrement(2019, 18, 10007),
                equalTo(findPositionOfCard(2019, shuffle(10007, listOf(ShuffleInstruction(DEAL_WITH_INCREMENT, BigInteger.valueOf(18))))))
            )
        }

        @Test
        internal fun `a cut by 200 moves card on index 2019 to index 1819 on a deck with 10007 cards`() {
            assertThat(
                cut(2019, 200, 10007),
                equalTo(findPositionOfCard(2019, shuffle(10007, listOf(ShuffleInstruction(CUT, BigInteger.valueOf(200))))))
            )
        }

        @Test
        internal fun `a cut by -200 moves card on index 2019 to index 9807 on a deck with 10007 cards`() {
            assertThat(
                cut(2019, -200, 10007),
                equalTo(findPositionOfCard(2019, shuffle(10007, listOf(ShuffleInstruction(CUT, BigInteger.valueOf(-200))))))
            )
        }

        @Test
        internal fun `a cut by -3893 moves card on index 6321 to index 207 on a deck with 10007 cards`() {
            assertThat(
                cut(6321, -3893, 10007),
                equalTo(findPositionOfCard(6321, shuffle(10007, listOf(ShuffleInstruction(CUT, BigInteger.valueOf(-3893))))))
            )
        }

        @Test
        internal fun `a cut by 200 moves card on index 100 to index 9907 on a deck with 10007 cards`() {
            assertThat(
                cut(100, 200, 10007),
                equalTo(findPositionOfCard(100, shuffle(10007, listOf(ShuffleInstruction(CUT, BigInteger.valueOf(200))))))
            )
        }

        @Test
        internal fun `a deal into new stack is deck length - index`() {
            assertThat(
                reverse(2019, 10007),
                equalTo(findPositionOfCard(2019, shuffle(10007, listOf(ShuffleInstruction(DEAL_INTO_NEW_STACK)))))
            )
        }

        @Test
        internal fun `do part 1 but faster`() {
            assertThat(
                shuffleJustOneCard(2019, 10007, parseInstructions(input)),
                equalTo(7395L)
            )
        }

        @Test
        internal fun `the inverse of reverse is reverse`() {
            assertThat(reverse(8772, 10007), equalTo(reverse(reverse(1234, 10007), 10007)))
        }

        @Test
        internal fun `the inverse of cut is`() {
            println(cut(1234, 555, 10007))
            assertThat(cutInverse(679, 555, 10007), equalTo(1234L))

            println(cut(123, 555, 10007))
            assertThat(cutInverse(9575, 555, 10007), equalTo(123L))

            println(cut(1234, -555, 10007))
            assertThat(cutInverse(1789, -555, 10007), equalTo(1234L))

            println(cut(123, -555, 10007))
            assertThat(cutInverse(678, -555, 10007), equalTo(123L))

            println(cut(9998, -555, 10007))
            assertThat(cutInverse(546, -555, 10007), equalTo(9998L))
        }

        @Test
        internal fun `the inverse of deal with increment is`() {
            println(dealWithIncrement(9, 3, 10))
            assertThat(dealWithIncrementInverse(7, 3, 10), equalTo(9L))

            println(dealWithIncrement(1234, 18, 10007))
            assertThat(dealWithIncrementInverse(2198, 18, 10007), equalTo(1234L))
        }

        @Test
        internal fun `reverse part 1`() {
            assertThat(inverseShuffleOneCard(7395, 10007, parseInstructions(input)), equalTo(2019L))
        }

        @Test
        internal fun `do it 10006 times`() {
            assertThat(inverseShuffleJustOneCardXTimes(2019, 10007, 10006, parseInstructions(input)), equalTo(2019L))
        }

        @Test
        internal fun `part 2`() {
//           See CondensingTheInputTest
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
