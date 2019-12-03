package com.github.shmvanhouten.adventofcode2019.day02

import java.lang.Exception
import java.lang.IllegalStateException
import java.lang.RuntimeException
import kotlin.Int.Companion.MAX_VALUE

fun findTargetOutput(intCode: IntCode, target: Int): Pair<Int, Int> {
    for (i in 0..MAX_VALUE) {
        for (j in 0..i) {
            val result: Int = try {
                run(intCode.withInitialParameters(i, j))[0]
            } catch (e: Exception) {
                -1
            }

            if(result == target) {
                return i to j
            }
        }
    }
    throw IllegalStateException("Not found")
}

fun run(intCode: IntCode): List<Int> {
    val intCodes = intCode.ints.toMutableList()
    (0..intCodes.size).step(4).forEach {pointer ->
        val instruction = getInstruction(intCodes, pointer)
        val targetIndex = instruction[3]
        when (instruction[0]) {
            1 -> intCodes[targetIndex] = intCodes[instruction[1]] + intCodes[instruction[2]]
            2 -> intCodes[targetIndex] = intCodes[instruction[1]] * intCodes[instruction[2]]
            99 -> return intCodes
            else -> throw RuntimeException("Unknown opcode: ${intCodes[0]}")
        }
    }
    return intCodes
}

private fun getInstruction(
    intCodes: MutableList<Int>,
    pointer: Int
): List<Int> {
    if(intCodes[pointer] == 99) {
        return listOf(99, 0, 0, 0)
    } else {
        return intCodes.subList(pointer, pointer + 4)
    }
}

class IntCode(val ints: List<Int>) {
    fun withInitialParameters(i: Int, j: Int): IntCode {
        val mutableInts = ints.toMutableList()
        mutableInts[1] = i
        mutableInts[2] = j
        return IntCode(mutableInts.toList())
    }

    constructor(vararg integers: Int) : this(integers.toList().toMutableList())
}


