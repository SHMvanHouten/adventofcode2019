package com.github.shmvanhouten.adventofcode2019.day22

import com.github.shmvanhouten.adventofcode2019.day22.Instructiontype.*
import com.github.shmvanhouten.adventofcode2019.util.findNeededPowersOf2ToGetTarget
import com.github.shmvanhouten.adventofcode2019.util.powerOf2sUntilN
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import java.math.BigInteger

class CondensingTheInputTest {

    @Test
    internal fun `deal into stack and cut x is the same as cut -x and deal into stack`() {
        assertThat(
            shuffleToList(
                10007,
                listOf(
                    ShuffleInstruction(DEAL_INTO_NEW_STACK),
                    ShuffleInstruction(CUT, BigInteger.valueOf(3))
                )
            ),
            equalTo(
                shuffleToList(
                    10007,
                    listOf(
                        ShuffleInstruction(CUT, BigInteger.valueOf(-3)),
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
                    ShuffleInstruction(CUT, BigInteger.valueOf(-3))
                )
            ),
            equalTo(
                shuffleToList(
                    10007,
                    listOf(
                        ShuffleInstruction(CUT, BigInteger.valueOf(3)),
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
                    ShuffleInstruction(CUT, BigInteger.valueOf(4)),
                    ShuffleInstruction(CUT, BigInteger.valueOf(3))
                )
            ),
            equalTo(
                shuffleToList(
                    10007,
                    listOf(
                        ShuffleInstruction(CUT, BigInteger.valueOf(7))
                    )
                )
            )
        )
        assertThat(
            shuffleToList(
                10007,
                listOf(
                    ShuffleInstruction(CUT, BigInteger.valueOf(10000)),
                    ShuffleInstruction(CUT, BigInteger.valueOf(10))
                )
            ),
            equalTo(
                shuffleToList(
                    10007,
                    listOf(
                        ShuffleInstruction(CUT, BigInteger.valueOf(3))
                    )
                )
            )
        )
        assertThat(
            shuffleToList(
                10007,
                listOf(
                    ShuffleInstruction(CUT, BigInteger.valueOf(4)),
                    ShuffleInstruction(CUT, BigInteger.valueOf(-3))
                )
            ),
            equalTo(
                shuffleToList(
                    10007,
                    listOf(
                        ShuffleInstruction(CUT, BigInteger.valueOf(1))
                    )
                )
            )
        )
        assertThat(
            shuffleToList(
                10007,
                listOf(
                    ShuffleInstruction(CUT, BigInteger.valueOf(-4)),
                    ShuffleInstruction(CUT, BigInteger.valueOf(-3))
                )
            ),
            equalTo(
                shuffleToList(
                    10007,
                    listOf(
                        ShuffleInstruction(CUT, BigInteger.valueOf(-7))
                    )
                )
            )
        )
        assertThat(
            shuffleToList(
                10007,
                listOf(
                    ShuffleInstruction(CUT, BigInteger.valueOf(-10000)),
                    ShuffleInstruction(CUT, BigInteger.valueOf(-10))
                )
            ),
            equalTo(
                shuffleToList(
                    10007,
                    listOf(
                        ShuffleInstruction(CUT, BigInteger.valueOf(-3))
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
            shuffleToList(10007,
                listOf(
                    ShuffleInstruction(CUT, BigInteger.valueOf(4)),
                    ShuffleInstruction(DEAL_WITH_INCREMENT, BigInteger.valueOf(3))
                )
            ),
            equalTo(
                shuffleToList(10007,
                    listOf(
                        ShuffleInstruction(DEAL_WITH_INCREMENT, BigInteger.valueOf(3)),
                        ShuffleInstruction(CUT, BigInteger.valueOf((3 * 4)))
                    )
                )
            )
        )
        assertThat(
            shuffleToList(10007,
                listOf(
                    ShuffleInstruction(CUT, BigInteger.valueOf(-4)),
                    ShuffleInstruction(DEAL_WITH_INCREMENT, BigInteger.valueOf(3))
                )
            ),
            equalTo(
                shuffleToList(10007,
                    listOf(
                        ShuffleInstruction(DEAL_WITH_INCREMENT, BigInteger.valueOf(3)),
                        ShuffleInstruction(CUT, BigInteger.valueOf((3 * -4)))
                    )
                )
            )
        )
        assertThat(
            shuffleToList(10007,
                listOf(
                    ShuffleInstruction(CUT, BigInteger.valueOf(10000)),
                    ShuffleInstruction(DEAL_WITH_INCREMENT, BigInteger.valueOf(18))
                )
            ),
            equalTo(
                shuffleToList(10007,
                    listOf(
                        ShuffleInstruction(DEAL_WITH_INCREMENT, BigInteger.valueOf(18)),
                        ShuffleInstruction(CUT, BigInteger.valueOf((10000 * 18 % 10007)))
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
                    ShuffleInstruction(DEAL_WITH_INCREMENT, BigInteger.valueOf(4)),
                    ShuffleInstruction(DEAL_WITH_INCREMENT, BigInteger.valueOf(3))
                )
            ),
            equalTo(
                shuffleToList(
                    10007,
                    listOf(
                        ShuffleInstruction(DEAL_WITH_INCREMENT, BigInteger.valueOf(3 * 4))
                    )
                )
            )
        )
        assertThat(
            shuffleToList(
                10007,
                listOf(
                    ShuffleInstruction(DEAL_WITH_INCREMENT, BigInteger.valueOf(18)),
                    ShuffleInstruction(DEAL_WITH_INCREMENT, BigInteger.valueOf(20))
                )
            ),
            equalTo(
                shuffleToList(
                    10007,
                    listOf(
                        ShuffleInstruction(DEAL_WITH_INCREMENT, BigInteger.valueOf(18 * 20))
                    )
                )
            )
        )
    }

    @Test
    internal fun `deal with increment (stackSize - 1) is reverse and cut -1`() {
        assertThat(
            shuffleToList(10007, listOf(ShuffleInstruction(DEAL_WITH_INCREMENT, BigInteger.valueOf(10006)))),
            equalTo(
                shuffleToList(10007,listOf(ShuffleInstruction(DEAL_INTO_NEW_STACK), ShuffleInstruction(CUT, BigInteger.valueOf(-1)))
                )
            )
        )
        assertThat(
            shuffleToList(10007,listOf(ShuffleInstruction(DEAL_WITH_INCREMENT, BigInteger.valueOf(10006)))),
            equalTo(
                shuffleToList(10007, listOf(ShuffleInstruction(CUT, BigInteger.valueOf(1)), ShuffleInstruction(DEAL_INTO_NEW_STACK))
                )
            )
        )
    }

    @Test
    internal fun `deal into stack, deal with increment x is equivalent to deal with increment -x, cut -(x - 1), deal into stack`() {
        //deal into stack                deal with increment -x
        //deal with increment x <==>     cut x - 1
        //                               deal into stack
        assertThat(
            shuffleToList(10007, listOf(ShuffleInstruction(DEAL_INTO_NEW_STACK), ShuffleInstruction(DEAL_WITH_INCREMENT, BigInteger.valueOf(5209)))),
            equalTo(
                shuffleToList(10007, listOf(ShuffleInstruction(DEAL_WITH_INCREMENT, BigInteger.valueOf(-5209)), ShuffleInstruction(CUT, BigInteger.valueOf(-5208)), ShuffleInstruction(DEAL_INTO_NEW_STACK)))
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
                    ShuffleInstruction(CUT, BigInteger.valueOf(5)),
                    ShuffleInstruction(DEAL_INTO_NEW_STACK)
                )
            ),
            equalTo(
                shuffleToList(10007, listOf(ShuffleInstruction(CUT, BigInteger.valueOf(-5))))
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
                    ShuffleInstruction(CUT, BigInteger.valueOf(18)),
                    ShuffleInstruction(DEAL_INTO_NEW_STACK),
                    ShuffleInstruction(CUT, BigInteger.valueOf(5))
                )
            ),
            equalTo(
                shuffleToList(
                    10007,
                    listOf(
                        ShuffleInstruction(CUT, BigInteger.valueOf(18 - 5)),
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
                    ShuffleInstruction(CUT, BigInteger.valueOf(18)),
                    ShuffleInstruction(DEAL_INTO_NEW_STACK),
                    ShuffleInstruction(CUT, BigInteger.valueOf(5))
                )
            ),
            equalTo(
                shuffleToList(
                    10007,
                    listOf(
                        ShuffleInstruction(DEAL_INTO_NEW_STACK),
                        ShuffleInstruction(CUT, BigInteger.valueOf(5 - 18))
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
                    ShuffleInstruction(CUT, BigInteger.valueOf(94)),
                    ShuffleInstruction(DEAL_WITH_INCREMENT, BigInteger.valueOf(18)),
                    ShuffleInstruction(CUT, BigInteger.valueOf(5))
                )
            ),
            equalTo(
                shuffleToList(
                    10007,
                    listOf(
                        ShuffleInstruction(DEAL_WITH_INCREMENT, BigInteger.valueOf(18)),
                        ShuffleInstruction(CUT, BigInteger.valueOf(94 * 18 + 5 % 10007))
                    )
                )
            )
        )
    }

    @Test
    internal fun `so this should also condense`() {
        val instructions = listOf(
            ShuffleInstruction(type = CUT, number = BigInteger.valueOf(-1549130178654)),
            ShuffleInstruction(type = DEAL_WITH_INCREMENT, number = BigInteger.valueOf(-7446)),
            ShuffleInstruction(type = CUT, number = BigInteger.valueOf(55352327)),
            ShuffleInstruction(type = DEAL_WITH_INCREMENT, number = BigInteger.valueOf(75))
        )
        val condensed = listOf(
            ShuffleInstruction(type = DEAL_WITH_INCREMENT, number = BigInteger.valueOf(-7446)),
            ShuffleInstruction(type = CUT, number = BigInteger.valueOf((-1549130178654 * 7446))),
            ShuffleInstruction(type = CUT, number = BigInteger.valueOf(55352327)),
            ShuffleInstruction(type = DEAL_WITH_INCREMENT, number = BigInteger.valueOf(75))
        )
        assertThat(
            shuffleToList(10007, condensed),
            equalTo(shuffleToList(10007, instructions))
            )
    }

    @Test
    internal fun `and this`() {
        val instructions = listOf(
            ShuffleInstruction(type = DEAL_WITH_INCREMENT, number = BigInteger.valueOf(11610)),
            ShuffleInstruction(type = DEAL_INTO_NEW_STACK),
            ShuffleInstruction(type = DEAL_WITH_INCREMENT, number = BigInteger.valueOf(-7328813184000)),
            ShuffleInstruction(type = CUT, number = BigInteger("19444103462680512001"))
        )
        val condensed = listOf(
            ShuffleInstruction(type = DEAL_WITH_INCREMENT, number = BigInteger("85087521066240000")),
            ShuffleInstruction(type = CUT, number = BigInteger("-7328813183999")),
            ShuffleInstruction(type = DEAL_INTO_NEW_STACK),
            ShuffleInstruction(type = CUT, number = BigInteger("19444103462680512001"))
        )

        assertThat(
            shuffleToList(23, condensed),
            equalTo(shuffleToList(23, instructions))
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
            equalTo(listOf(ShuffleInstruction(DEAL_WITH_INCREMENT, BigInteger.valueOf(270)), ShuffleInstruction(CUT, BigInteger.valueOf(-61480))))
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
                    ShuffleInstruction(DEAL_WITH_INCREMENT, BigInteger.valueOf(11610)),
                    ShuffleInstruction(CUT, BigInteger.valueOf(-2645732))
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

    @Test
    internal fun `condensed instructions`() {
        val condensedInstructions = listOf(
            ShuffleInstruction(type=DEAL_WITH_INCREMENT, number= BigInteger("667487277673079497859196426642502502538645344354304000000000000000000")),
            ShuffleInstruction(type=DEAL_INTO_NEW_STACK),
            ShuffleInstruction(type=CUT, number= BigInteger("152537919484486375356238506338573667307849959994388909641242808361847428"))
        )
        assertThat(
            shuffleToList(10007, condensedInstructions),
            equalTo(shuffleToList(10007, parseInstructions(input)))
        )
    }

    @Test
    internal fun bla() {
        val instructions = listOf(
            ShuffleInstruction(type=DEAL_WITH_INCREMENT, number= BigInteger("667487277673079497859196426642502502538645344354304000000000000000000")),
            ShuffleInstruction(type=DEAL_INTO_NEW_STACK),
            ShuffleInstruction(type=CUT, number= BigInteger("152537919484486375356238506338573667307849959994388909641242808361847428")),
            ShuffleInstruction(type=DEAL_WITH_INCREMENT, number= BigInteger("667487277673079497859196426642502502538645344354304000000000000000000")),
            ShuffleInstruction(type=DEAL_INTO_NEW_STACK),
            ShuffleInstruction(type=CUT, number= BigInteger("152537919484486375356238506338573667307849959994388909641242808361847428"))
        )

        val condensed = listOf(
            ShuffleInstruction(type=DEAL_WITH_INCREMENT, number= BigInteger("667487277673079497859196426642502502538645344354304000000000000000000").times(BigInteger("-667487277673079497859196426642502502538645344354304000000000000000000"))),
            ShuffleInstruction(type=CUT, number= BigInteger("667487277673079497859196426642502502538645344354304000000000000000000").minus(BigInteger.ONE).negate() + BigInteger("-152537919484486375356238506338573667307849959994388909641242808361847428") * (BigInteger("667487277673079497859196426642502502538645344354304000000000000000000") - BigInteger.ONE))
        ).map { performModulusOnSize(it, 119315717514047) }
        println(condensed)

        val doubleCondensed = listOf(
            ShuffleInstruction(DEAL_WITH_INCREMENT, BigInteger.valueOf(97659969542601)),
            ShuffleInstruction(CUT, BigInteger.valueOf(50103195044617))
        )
        val testing = listOf(
            ShuffleInstruction(DEAL_WITH_INCREMENT, BigInteger.valueOf(97659969542601) * BigInteger.valueOf(97659969542601)),
            ShuffleInstruction(CUT, BigInteger.valueOf(50103195044617) * BigInteger.valueOf(97659969542601) + BigInteger.valueOf(50103195044617))
        ).map { performModulusOnSize(it, 119315717514047) }
        val quadCondensed = listOf(
            ShuffleInstruction(DEAL_WITH_INCREMENT, BigInteger.valueOf(108924598018962)),
            ShuffleInstruction(CUT, BigInteger.valueOf(11364558223257))
        )
        val anotherQuadCondensed = listOf(
            ShuffleInstruction(DEAL_WITH_INCREMENT, BigInteger.valueOf(-21655747971446) * BigInteger.valueOf(-21655747971446)),
            ShuffleInstruction(CUT, BigInteger.valueOf(-69212522469430) * (BigInteger.valueOf(-21655747971446) - BigInteger.ONE))
        ).map { performModulusOnSize(it, 119315717514047) }
        assertThat(testing, equalTo(quadCondensed))


        // decksize = 41
        // dwi = -1234
        // 41 - (1234 % 41) == 41 - (-1234 % 41)
    }

    @Test
    internal fun `condensed instructions x times`() {
        val instructions = parseInstructions(input)
        val condensedInstructions = condenseInstructions(instructions).map { performModulusOnSize(it, 10007) }
        val doubleCondensed = condenseInstructions(condensedInstructions + condensedInstructions).map { performModulusOnSize(it, 10007) }
        val condensedTimes4 = condenseInstructions(doubleCondensed + doubleCondensed).map { performModulusOnSize(it, 10007) }
        val condensedTimes8 = condenseInstructions(condensedTimes4 + condensedTimes4).map { performModulusOnSize(it, 10007) }
        val condensedTimes16 = condenseInstructions(condensedTimes8 + condensedTimes8).map { performModulusOnSize(it, 10007) }
        val condensedTimes32 = condenseInstructions(condensedTimes16 + condensedTimes16).map { performModulusOnSize(it, 10007) }
        val condensedTimes64 = condenseInstructions(condensedTimes32 + condensedTimes32).map { performModulusOnSize(it, 10007) }
        println(condensedTimes64.size)
        assertThat(
            findPositionOfCard(2019, shuffle(10007, condensedTimes64)),
            equalTo(shuffleJustOneCardXTimes(2019, 10007, 64, parseInstructions(input)))
        )
    }

    @Test
    internal fun `part 1 shuffle fast test`() {
        val instructions = parseInstructions(input)
        assertThat(
            shuffleOneCardFast(2019, 10007, 101741582076661, instructions),
            equalTo(shuffleJustOneCardXTimes(2019, 10007, 101741582076661 % 10006, instructions))
        )
    }

    @Test
    internal fun `part2 test less shuffles`() {
        val instructions = listOf(
            ShuffleInstruction(type=DEAL_WITH_INCREMENT, number= BigInteger("667487277673079497859196426642502502538645344354304000000000000000000")),
            ShuffleInstruction(type=DEAL_INTO_NEW_STACK),
            ShuffleInstruction(type=CUT, number= BigInteger("152537919484486375356238506338573667307849959994388909641242808361847428"))
        ).map { performModulusOnSize(it, 119315717514047) }
        assertThat(
            shuffleOneCardFast(2020, 119315717514047, 1, instructions + instructions),
            equalTo(shuffleJustOneCardXTimes(2020, 119315717514047, 2, parseInstructions(input)))
        )
        0.until(200L).forEach { i ->
            if (shuffleOneCardFast(2020, 119315717514047, 2, instructions) == shuffleJustOneCardXTimes(2020, 119315717514047, i, parseInstructions(input))) {
                println(i)
            }
        }
        assertThat(
            shuffleOneCardFast(2020, 119315717514047, 3, instructions),
            equalTo(shuffleJustOneCardXTimes(2020, 119315717514047, 3, parseInstructions(input)))
        )
    }

    @Test
    internal fun part2() {
        // we saw for the "simple" input (10007) that the pattern repeats after exactly 10006 runs
        // so instead of trying to implement an inverse deal with increment function that doesn't take forever for this massive input
        // I figured I could get the same result if we just loop the other way.
        // It might still be interesting to learn how to do an inverse function though.
        assertThat(
            shuffleOneCardFast(2020, 119315717514047, 119315717514047 - (101741582076661 + 1), parseInstructions(input)),
            equalTo(32376123569821L)
        )
    }

    @Test
    internal fun `turns out we actually have to get the element at index 2020, not the index of the element 2020`() {
        assertThat(shuffle(23, parseInstructions(input))[9],
            equalTo(inverseShuffleOneCard(9, 23, parseInstructions(input))))
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