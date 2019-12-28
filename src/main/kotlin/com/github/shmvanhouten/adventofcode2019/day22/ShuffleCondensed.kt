package com.github.shmvanhouten.adventofcode2019.day22

import com.github.shmvanhouten.adventofcode2019.util.findNeededPowersOf2ToGetTarget
import java.math.BigInteger

fun shuffleOneCardFast(
    originalIndex: Long,
    deckSize: Long,
    amountOfShuffles: Long,
    instructions: List<ShuffleInstruction>
): Long {
    val condensedInstructions = condenseInstructionsUntilTargetIsReached(instructions, deckSize, amountOfShuffles)

    return shuffleJustOneCard(originalIndex, deckSize, condensedInstructions)
}

fun condenseInstructionsUntilTargetIsReached(
    instructions: List<ShuffleInstruction>,
    deckSize: Long,
    amountOfShuffles: Long
): List<ShuffleInstruction> {
    val condensedInstructions = condenseInstructions(instructions).map { performModulusOnSize(it, deckSize) }

    val powersOf2 = findNeededPowersOf2ToGetTarget(BigInteger.valueOf(amountOfShuffles))
    val powerToCondensedInstruction = condenseInstructionsForEachPowerUntil(condensedInstructions, powersOf2.max()!!, deckSize)
    return powersOf2.map { powerToCondensedInstruction[it]!! }.flatten()

//    return condenseUntilTargetIsReached(powersOf2, powerToCondensedInstruction, deckSize)
}

private fun condenseUntilTargetIsReached(
    powersOf2: List<Int>,
    powerToCondensedInstruction: Map<Int, List<ShuffleInstruction>>,
    deckSize: Long
): List<ShuffleInstruction> {
    var instructions = emptyList<ShuffleInstruction>()
    powersOf2.forEach { powerOf2 ->
        instructions = condenseInstructions(instructions + powerToCondensedInstruction[powerOf2]!!)
            .map { performModulusOnSize(it, deckSize) }
    }

    return instructions
}

fun condenseInstructionsForEachPowerUntil(
    instructions: List<ShuffleInstruction>,
    max: Int,
    deckSize: Long
): Map<Int, List<ShuffleInstruction>> {
    var powerOf2 = 0
    var lastCondensedInstructions = instructions
    val powerToCondensedInstruction = mutableListOf(powerOf2 to lastCondensedInstructions)
    while (powerOf2++ < max) {
        lastCondensedInstructions = if(powerOf2 == 1) {
            lastCondensedInstructions + lastCondensedInstructions
        } else if(powerOf2 == 2) {
            lastCondensedInstructions + lastCondensedInstructions
        }else {
            condenseInstructions(lastCondensedInstructions + lastCondensedInstructions)
                .map { performModulusOnSize(it, deckSize) }
        }
        powerToCondensedInstruction += powerOf2 to lastCondensedInstructions
    }
    return powerToCondensedInstruction.toMap()
}
