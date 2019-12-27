package com.github.shmvanhouten.adventofcode2019.day22

import com.github.shmvanhouten.adventofcode2019.day22.Instructiontype.*
import java.math.BigInteger

fun parseInstructions(input: String): List<ShuffleInstruction> {
    return input.split('\n')
        .map { toShuffleInstruction(it) }
}

fun toShuffleInstruction(instruction: String): ShuffleInstruction {
    val words = instruction.split(' ')
    return when {
        words[0] == "cut" -> ShuffleInstruction(CUT, BigInteger(words[1]))
        words[2] == "increment" -> ShuffleInstruction(
            DEAL_WITH_INCREMENT,
            BigInteger(words[3])
        )
        else -> ShuffleInstruction(DEAL_INTO_NEW_STACK)
    }
}
