package com.github.shmvanhouten.adventofcode2019.day22

import java.math.BigInteger

data class ShuffleInstruction(val type: Instructiontype, val number: BigInteger = BigInteger.ONE)

enum class Instructiontype {
    DEAL_WITH_INCREMENT,
    CUT,
    DEAL_INTO_NEW_STACK
}
