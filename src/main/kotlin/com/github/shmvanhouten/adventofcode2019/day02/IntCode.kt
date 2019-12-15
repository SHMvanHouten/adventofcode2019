package com.github.shmvanhouten.adventofcode2019.day02

class IntCode(val ints: Map<Long, Long>) {
    fun withInitialParameters(i: Long, j: Long): IntCode {
        val mutableInts = ints.toMutableMap()
        mutableInts[1] = i
        mutableInts[2] = j
        return IntCode(mutableInts)
    }

    constructor(vararg codes: Long) : this(codes.toList().mapIndexed { i, value -> i.toLong() to value }.toMap())
    constructor(codes: List<Long>) : this(codes.mapIndexed { i, value -> i.toLong() to value }.toMap())
}
