package com.github.shmvanhouten.adventofcode2019.day02

class IntCode(val ints: List<Int>) {
    fun withInitialParameters(i: Int, j: Int): IntCode {
        val mutableInts = ints.toMutableList()
        mutableInts[1] = i
        mutableInts[2] = j
        return IntCode(mutableInts.toList())
    }

    constructor(vararg integers: Int) : this(integers.toList().toMutableList())
}
