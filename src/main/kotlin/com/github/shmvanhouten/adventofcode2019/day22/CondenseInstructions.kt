package com.github.shmvanhouten.adventofcode2019.day22

import com.github.shmvanhouten.adventofcode2019.day22.Instructiontype.*
import java.math.BigInteger

fun condenseInstructions(instructions: List<ShuffleInstruction>): List<ShuffleInstruction> {
    var previousSize = instructions.size + 1
    var condensedInstructions = instructions
    while (previousSize > condensedInstructions.size) {
        previousSize = condensedInstructions.size
        condensedInstructions = condenseCutsAndDWIs(condensedInstructions)
        condensedInstructions = condenseCutsAndReverses(condensedInstructions)
        condensedInstructions = condenseReverseAndDWIs(condensedInstructions)
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

private fun condenseReverseAndDWIs(instructions: List<ShuffleInstruction>): List<ShuffleInstruction> {
    var previousSize = instructions.size + 1
    var condensedInstructions = instructions
    while (previousSize > condensedInstructions.size) {
        previousSize = condensedInstructions.size
        condensedInstructions = changeReverseAndDwiIntoDWICutAndReverse(condensedInstructions)
    }
    return condensedInstructions
}

fun changeReverseAndDwiIntoDWICutAndReverse(instructions: List<ShuffleInstruction>): List<ShuffleInstruction> {
    var i = findIndexOfReverseAndDwi(instructions, 0)
    while (i < instructions.size) {
        val switchedInstructions = switchReverseAndDWIAtIndex(instructions, i)
        val compressedInstructions = compressInstructions(switchedInstructions)

        if (compressedInstructions.size < instructions.size) {
            return compressedInstructions
        } else {
            val hadOtherOperationsDone = switchCutAndDWI(compressedInstructions)
            if (hadOtherOperationsDone.size < instructions.size) {
                return hadOtherOperationsDone
            } else {
            i = findIndexOfReverseAndDwi(instructions, i + 1)
            }
        }
    }
    return instructions
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
            val hadOtherOperationsDone = switchCutAndReverse(compressedInstructions)
            if (hadOtherOperationsDone.size < instructions.size) {
                return hadOtherOperationsDone
            } else {
                i = findIndexWhereCutAndDWICanBeSwitched(shuffleInstructions, i + 1)
            }
        }
    }
    return shuffleInstructions
}

fun switchReverseAndDWIAtIndex(shuffleInstructions: List<ShuffleInstruction>, i: Int): List<ShuffleInstruction> {
    val instructions = shuffleInstructions.toMutableList()

    val dWIInstruction = shuffleInstructions[i + 1]
    instructions[i] = ShuffleInstruction(DEAL_WITH_INCREMENT, dWIInstruction.number.negate())
    instructions[i + 1] = ShuffleInstruction(CUT, (dWIInstruction.number.abs().minus(BigInteger.ONE)).negate())
    instructions.add(i + 2, ShuffleInstruction(DEAL_INTO_NEW_STACK))
    return instructions
}

private fun switchCutAndDwiAtIndex(
    shuffleInstructions: List<ShuffleInstruction>,
    i: Int
): MutableList<ShuffleInstruction> {
    val instructions = shuffleInstructions.toMutableList()

    val dWIInstruction = shuffleInstructions[i + 1]
    val cutInstruction = shuffleInstructions[i]
    instructions[i] = dWIInstruction
    instructions[i + 1] = ShuffleInstruction(CUT, cutInstruction.number * dWIInstruction.number.abs())
    return instructions
}

private fun switchCutAndReverseAtIndex(
    shuffleInstructions: List<ShuffleInstruction>,
    i: Int
): MutableList<ShuffleInstruction> {
    val instructions = shuffleInstructions.toMutableList()

    val first = shuffleInstructions[i]
    val second = shuffleInstructions[i + 1]

    if (first.type == CUT) {
        instructions[i] = second
        instructions[i + 1] = ShuffleInstruction(CUT, first.number.negate())
    } else {
        instructions[i] = ShuffleInstruction(CUT, second.number.negate())
        instructions[i + 1] = first
    }
    return instructions
}

fun findIndexOfReverseAndDwi(instructions: List<ShuffleInstruction>, index: Int): Int {
    return findIndexOfInstructionAndInstruction(index, instructions, DEAL_INTO_NEW_STACK, DEAL_WITH_INCREMENT)
}

fun findIndexWhereCutAndDWICanBeSwitched(shuffleInstructions: List<ShuffleInstruction>, index: Int): Int {
    return findIndexOfInstructionAndInstruction(index, shuffleInstructions, CUT, DEAL_WITH_INCREMENT)
}

private fun findIndexOfInstructionAndInstruction(
    index: Int,
    shuffleInstructions: List<ShuffleInstruction>,
    instruction1: Instructiontype,
    instruction2: Instructiontype
): Int {
    return index.until(shuffleInstructions.size - 1).find { i ->
        shuffleInstructions[i].type == instruction1 && shuffleInstructions[i + 1].type == instruction2
    } ?: shuffleInstructions.size
}

fun findIndexWhereCutAndReverseCanBeSwitched(shuffleInstructions: List<ShuffleInstruction>, index: Int): Int {
    return index.until(shuffleInstructions.size - 1).find { i ->
        (shuffleInstructions[i].type == CUT && shuffleInstructions[i + 1].type == DEAL_INTO_NEW_STACK) ||
                (shuffleInstructions[i].type == DEAL_INTO_NEW_STACK && shuffleInstructions[i + 1].type == CUT)
    } ?: shuffleInstructions.size
}

fun compressInstructions(shuffleInstructions: List<ShuffleInstruction>): List<ShuffleInstruction> {
    var compressed = doCompress(shuffleInstructions)
    var previousSize = shuffleInstructions.size
    while (compressed.size < previousSize) {
        previousSize = compressed.size
        compressed = doCompress(compressed)
    }
    return compressed
}

private fun doCompress(shuffleInstructions: List<ShuffleInstruction>): List<ShuffleInstruction> {
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
