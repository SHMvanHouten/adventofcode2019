package com.github.shmvanhouten.adventofcode2019.day22

import com.github.shmvanhouten.adventofcode2019.day22.Instructiontype.*

fun condenseInstructions(instructions: List<ShuffleInstruction>): List<ShuffleInstruction> {
    var previousSize = instructions.size + 1
    var condensedInstructions = instructions
    while (previousSize > condensedInstructions.size) {
        previousSize = condensedInstructions.size
        condensedInstructions = condenseCutsAndDWIs(condensedInstructions)
        condensedInstructions = condenseCutsAndReverses(condensedInstructions)
    }
    return condensedInstructions
}

fun condenseCutsAndReverses(instructions: List<ShuffleInstruction>): List<ShuffleInstruction> {
    var previousSize = instructions.size + 1
    var condensedInstructions = instructions
    while (previousSize > condensedInstructions.size) {
        previousSize = condensedInstructions.size
        condensedInstructions = switchCutAndReverse(condensedInstructions)
    }
    return condensedInstructions
}

private fun condenseCutsAndDWIs(instructions: List<ShuffleInstruction>): List<ShuffleInstruction> {
    var previousSize = instructions.size + 1
    var condensedInstructions = instructions
    while (previousSize > condensedInstructions.size) {
        previousSize = condensedInstructions.size
        condensedInstructions = switchCutAndDWI(condensedInstructions)
    }
    return condensedInstructions
}

fun switchCutAndReverse(shuffleInstructions: List<ShuffleInstruction>): List<ShuffleInstruction> {
    var i = findIndexWhereCutAndReverseCanBeSwitched(shuffleInstructions, 0)
    while (i < shuffleInstructions.size) {
        val instructions = switchCutAndReverseAtIndex(shuffleInstructions, i)

        val compressedInstructions = compressInstructions(instructions)

        if (compressedInstructions.size < shuffleInstructions.size) {
            return compressedInstructions
        } else {
            i = findIndexWhereCutAndReverseCanBeSwitched(shuffleInstructions, i + 1)
        }
    }
    return shuffleInstructions
}

fun switchCutAndDWI(shuffleInstructions: List<ShuffleInstruction>): List<ShuffleInstruction> {
    var i = findIndexWhereCutAndDWICanBeSwitched(shuffleInstructions, 0)
    while (i < shuffleInstructions.size) {
        val instructions = switchCutAndDwiAtIndex(shuffleInstructions, i)

        val compressedInstructions = compressInstructions(instructions)

        if (compressedInstructions.size < shuffleInstructions.size) {
            return compressedInstructions
        } else {
            i = findIndexWhereCutAndDWICanBeSwitched(shuffleInstructions, i + 1)
        }
    }
    return shuffleInstructions
}

private fun switchCutAndDwiAtIndex(
    shuffleInstructions: List<ShuffleInstruction>,
    i: Int
): MutableList<ShuffleInstruction> {
    val instructions = shuffleInstructions.toMutableList()

    val dWIInstruction = shuffleInstructions[i + 1]
    val cutInstruction = shuffleInstructions[i]
    instructions[i] = dWIInstruction
    instructions[i + 1] = ShuffleInstruction(CUT, cutInstruction.number * dWIInstruction.number)
    return instructions
}

private fun switchCutAndReverseAtIndex(
    shuffleInstructions: List<ShuffleInstruction>,
    i: Int
): MutableList<ShuffleInstruction> {
    val instructions = shuffleInstructions.toMutableList()

    val cutInstruction = shuffleInstructions[i]
    val reverse = shuffleInstructions[i + 1]
    instructions[i] = reverse
    instructions[i + 1] = ShuffleInstruction(CUT, -1 * cutInstruction.number)
    return instructions
}

fun findIndexWhereCutAndDWICanBeSwitched(shuffleInstructions: List<ShuffleInstruction>, index: Int): Int {
    return index.until(shuffleInstructions.size - 1).find { i ->
        shuffleInstructions[i].type == CUT && shuffleInstructions[i + 1].type == DEAL_WITH_INCREMENT
    } ?: shuffleInstructions.size
}

fun findIndexWhereCutAndReverseCanBeSwitched(shuffleInstructions: List<ShuffleInstruction>, index: Int): Int {
    return index.until(shuffleInstructions.size - 1).find { i ->
        shuffleInstructions[i].type == CUT && shuffleInstructions[i + 1].type == DEAL_INTO_NEW_STACK
    } ?: shuffleInstructions.size
}

fun compressInstructions(shuffleInstructions: List<ShuffleInstruction>): List<ShuffleInstruction> {
    val compressedInstructions = mutableListOf<ShuffleInstruction?>()
    var i = 0
    while (i < shuffleInstructions.size) {
        if (next2InstructionsAreTheSameType(i, shuffleInstructions)) {
            compressedInstructions.add(compress(shuffleInstructions[i], shuffleInstructions[i + 1]))
            i += 2
        } else {
            compressedInstructions.add(shuffleInstructions[i])
            i++
        }
    }

    return compressedInstructions.filterNotNull()
}

private fun next2InstructionsAreTheSameType(
    i: Int,
    shuffleInstructions: List<ShuffleInstruction>
) = i + 1 < shuffleInstructions.size && shuffleInstructions[i].type == shuffleInstructions[i + 1].type

fun compress(first: ShuffleInstruction, second: ShuffleInstruction): ShuffleInstruction? {
    return when (first.type) {
        DEAL_WITH_INCREMENT -> ShuffleInstruction(DEAL_WITH_INCREMENT, first.number * second.number)
        CUT -> ShuffleInstruction(CUT, first.number + second.number)
        DEAL_INTO_NEW_STACK -> null
    }
}
