package com.github.shmvanhouten.adventofcode2019.day22

import com.github.shmvanhouten.adventofcode2019.day22.Instructiontype.*
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@Nested
class CondensingTheInputTest {

    @Test
    internal fun `deal into stack and cut x is the same as cut -x and deal into stack`() {
        assertThat(
            shuffleToList(
                10007,
                listOf(
                    ShuffleInstruction(DEAL_INTO_NEW_STACK),
                    ShuffleInstruction(CUT, 3)
                )
            ),
            equalTo(
                shuffleToList(
                    10007,
                    listOf(
                        ShuffleInstruction(CUT, -3),
                        ShuffleInstruction(DEAL_INTO_NEW_STACK)
                    )
                )
            )
        )
        assertThat(
            shuffleToList(
                10007,
                listOf(
                    ShuffleInstruction(DEAL_INTO_NEW_STACK),
                    ShuffleInstruction(CUT, -3)
                )
            ),
            equalTo(
                shuffleToList(
                    10007,
                    listOf(
                        ShuffleInstruction(CUT, 3),
                        ShuffleInstruction(DEAL_INTO_NEW_STACK)
                    )
                )
            )
        )

    }

    @Test
    internal fun `cut x and cut y is cut(x + y) % stackSize`() {
        assertThat(
            shuffleToList(
                10007,
                listOf(
                    ShuffleInstruction(CUT, 4),
                    ShuffleInstruction(CUT, 3)
                )
            ),
            equalTo(
                shuffleToList(
                    10007,
                    listOf(
                        ShuffleInstruction(CUT, 7)
                    )
                )
            )
        )
        assertThat(
            shuffleToList(
                10007,
                listOf(
                    ShuffleInstruction(CUT, 10000),
                    ShuffleInstruction(CUT, 10)
                )
            ),
            equalTo(
                shuffleToList(
                    10007,
                    listOf(
                        ShuffleInstruction(CUT, 3)
                    )
                )
            )
        )
        assertThat(
            shuffleToList(
                10007,
                listOf(
                    ShuffleInstruction(CUT, 4),
                    ShuffleInstruction(CUT, -3)
                )
            ),
            equalTo(
                shuffleToList(
                    10007,
                    listOf(
                        ShuffleInstruction(CUT, 1)
                    )
                )
            )
        )
        assertThat(
            shuffleToList(
                10007,
                listOf(
                    ShuffleInstruction(CUT, -4),
                    ShuffleInstruction(CUT, -3)
                )
            ),
            equalTo(
                shuffleToList(
                    10007,
                    listOf(
                        ShuffleInstruction(CUT, -7)
                    )
                )
            )
        )
        assertThat(
            shuffleToList(
                10007,
                listOf(
                    ShuffleInstruction(CUT, -10000),
                    ShuffleInstruction(CUT, -10)
                )
            ),
            equalTo(
                shuffleToList(
                    10007,
                    listOf(
                        ShuffleInstruction(CUT, -3)
                    )
                )
            )
        )
    }

    @Test
    internal fun `cut x and deal with increment y is deal with increment y and cut (x * y % stackSize)`() {
        // cut x                         deal with increment y
        // deal with increment y   <==>  cut (x * y)

        // (as long as all numbers in the end  are % stackSize
        assertThat(
            shuffleToList(
                10007,
                listOf(
                    ShuffleInstruction(CUT, 4),
                    ShuffleInstruction(DEAL_WITH_INCREMENT, 3)
                )
            ),
            equalTo(
                shuffleToList(
                    10007,
                    listOf(
                        ShuffleInstruction(DEAL_WITH_INCREMENT, 3),
                        ShuffleInstruction(CUT, (3 * 4))
                    )
                )
            )
        )
        assertThat(
            shuffleToList(
                10007,
                listOf(
                    ShuffleInstruction(CUT, -4),
                    ShuffleInstruction(DEAL_WITH_INCREMENT, 3)
                )
            ),
            equalTo(
                shuffleToList(
                    10007,
                    listOf(
                        ShuffleInstruction(DEAL_WITH_INCREMENT, 3),
                        ShuffleInstruction(CUT, (3 * -4))
                    )
                )
            )
        )
        assertThat(
            shuffleToList(
                10007,
                listOf(
                    ShuffleInstruction(CUT, 10000),
                    ShuffleInstruction(DEAL_WITH_INCREMENT, 18)
                )
            ),
            equalTo(
                shuffleToList(
                    10007,
                    listOf(
                        ShuffleInstruction(DEAL_WITH_INCREMENT, 18),
                        ShuffleInstruction(CUT, (10000 * 18 % 10007))
                    )
                )
            )
        )
    }

    @Test
    internal fun `deal with increment x and deal with increment y is deal with increment x * y`() {
        assertThat(
            shuffleToList(
                10007,
                listOf(
                    ShuffleInstruction(DEAL_WITH_INCREMENT, 4),
                    ShuffleInstruction(DEAL_WITH_INCREMENT, 3)
                )
            ),
            equalTo(
                shuffleToList(
                    10007,
                    listOf(
                        ShuffleInstruction(DEAL_WITH_INCREMENT, 3 * 4)
                    )
                )
            )
        )
        assertThat(
            shuffleToList(
                10007,
                listOf(
                    ShuffleInstruction(DEAL_WITH_INCREMENT, 18),
                    ShuffleInstruction(DEAL_WITH_INCREMENT, 20)
                )
            ),
            equalTo(
                shuffleToList(
                    10007,
                    listOf(
                        ShuffleInstruction(DEAL_WITH_INCREMENT, 18 * 20)
                    )
                )
            )
        )
    }

    @Test
    internal fun `deal with increment (stackSize - 1) is reverse and cut -1`() {
        assertThat(
            shuffleToList(
                10007,
                listOf(ShuffleInstruction(DEAL_WITH_INCREMENT, 10006))
            ),
            equalTo(
                shuffleToList(
                    10007,
                    listOf(
                        ShuffleInstruction(DEAL_INTO_NEW_STACK),
                        ShuffleInstruction(CUT, -1)
                    )
                )
            )
        )
        assertThat(
            shuffleToList(
                10007,
                listOf(ShuffleInstruction(DEAL_WITH_INCREMENT, 10006))
            ),
            equalTo(
                shuffleToList(
                    10007,
                    listOf(
                        ShuffleInstruction(CUT, 1),
                        ShuffleInstruction(DEAL_INTO_NEW_STACK)
                    )
                )
            )
        )
    }


    @Test
    internal fun `so deal into stack cut x deal into stack is the same as cut -x`() {
        // deal into stack          cut -x
        // cut x              <=>
        // deal into stack
        assertThat(
            shuffleToList(
                10007, listOf(
                    ShuffleInstruction(DEAL_INTO_NEW_STACK),
                    ShuffleInstruction(CUT, 5),
                    ShuffleInstruction(DEAL_INTO_NEW_STACK)
                )
            ),
            equalTo(
                shuffleToList(10007, listOf(ShuffleInstruction(CUT, -5)))
            )
        )
    }

    @Test
    internal fun `so cut x, deal into stack, cut y is the same as cut x - y, deal into stack`() {
        // cut x                 cut(x - y)            deal into stack
        // deal into stack  <=>  deal into stack  <=>  cut(y - x)
        // cut y
        assertThat(
            shuffleToList(
                10007,
                listOf(
                    ShuffleInstruction(CUT, 18),
                    ShuffleInstruction(DEAL_INTO_NEW_STACK),
                    ShuffleInstruction(CUT, 5)
                )
            ),
            equalTo(
                shuffleToList(
                    10007,
                    listOf(
                        ShuffleInstruction(CUT, 18 - 5),
                        ShuffleInstruction(DEAL_INTO_NEW_STACK)
                    )
                )
            )
        )
    }

    @Test
    internal fun `so cut x, deal into stack, cut y is the same as deal into stack, cut y - x`() {
        assertThat(
            shuffleToList(
                10007,
                listOf(
                    ShuffleInstruction(CUT, 18),
                    ShuffleInstruction(DEAL_INTO_NEW_STACK),
                    ShuffleInstruction(CUT, 5)
                )
            ),
            equalTo(
                shuffleToList(
                    10007,
                    listOf(
                        ShuffleInstruction(DEAL_INTO_NEW_STACK),
                        ShuffleInstruction(CUT, 5 - 18)
                    )
                )
            )
        )
    }

    @Test
    internal fun `so cut x, deal with increment y, cut z is the same as deal with increment y and cut (x * y + z % stackSize)`() {
        // cut x                      deal with increment y
        // deal with increment y <=>  cut(x * y + z % stackSize)
        // cut z
        assertThat(
            shuffleToList(
                10007,
                listOf(
                    ShuffleInstruction(CUT, 94),
                    ShuffleInstruction(DEAL_WITH_INCREMENT, 18),
                    ShuffleInstruction(CUT, 5)
                )
            ),
            equalTo(
                shuffleToList(
                    10007,
                    listOf(
                        ShuffleInstruction(DEAL_WITH_INCREMENT, 18),
                        ShuffleInstruction(CUT, 94 * 18 + 5 % 10007)
                    )
                )
            )
        )
    }

    @Test
    internal fun `part 1 condensed`() {
        assertThat(
            shuffleToList(10007, parseInstructions(condensedInput)),
            equalTo(
                shuffleToList(10007, parseInstructions(input))
            )
        )
    }

    @Test
    internal fun `condense deals with increments and cuts`() {
        //deal with increment 18            deal with increment 18 * 15
        //cut -3893                         cut (15 * -3893) - 3085
        //deal with increment 15
        //cut -3085
        val input = """deal with increment 18
cut -3893
deal with increment 15
cut -3085"""
        val instructions = parseInstructions(input)
        assertThat(
            condenseInstructions(instructions),
            equalTo(listOf(ShuffleInstruction(DEAL_WITH_INCREMENT, 270), ShuffleInstruction(CUT, -61480)))
        )
    }

    @Test
    internal fun `condense deals with increments and cuts 2`() {
        val input = """deal with increment 18
cut -3893
deal with increment 15
cut -3085
deal with increment 43
cut -2092"""
        val instructions = parseInstructions(input)
        assertThat(
            condenseInstructions(instructions),
            equalTo(
                listOf(
                    ShuffleInstruction(DEAL_WITH_INCREMENT, 270),
                    ShuffleInstruction(CUT, -61480),
                    ShuffleInstruction(DEAL_WITH_INCREMENT, 43),
                    ShuffleInstruction(CUT, -2092)
                )
            )
        )
    }

    @Test
    internal fun `condense input`() {
        val instructions = parseInstructions(input)
        val condensedInstructions = condenseInstructions(instructions)
        println(condensedInstructions)
        println("condensed from ${instructions.size} to ${condensedInstructions.size}")

        assertThat(
            shuffleToList(10007, condensedInstructions),
            equalTo(shuffleToList(10007, instructions))
        )
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

    private val condensedInput = """deal with increment 11610
deal into new stack
cut 2653104
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