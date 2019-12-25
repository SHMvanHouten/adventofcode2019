package com.github.shmvanhouten.adventofcode2019.day22

data class ShuffleInstruction(val type: Instructiontype, val number: Int = -1)

enum class Instructiontype {
    DEAL_WITH_INCREMENT,
    CUT,
    DEAL_INTO_NEW_STACK
}
