package com.github.shmvanhouten.adventofcode2019.day02

class IntCode(val ints: List<Long>) {
    fun withInitialParameters(i: Long, j: Long): IntCode {
        val mutableInts = ints.toMutableList()
        mutableInts[1] = i
        mutableInts[2] = j
        return IntCode(mutableInts.toList())
    }

    constructor(vararg codes: Long) : this(codes.toList().toMutableList())
}
