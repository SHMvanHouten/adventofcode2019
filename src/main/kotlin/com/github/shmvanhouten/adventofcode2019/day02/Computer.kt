package com.github.shmvanhouten.adventofcode2019.day02

import java.util.*

private const val END_CODE = "99"
private const val ADD_CODE = "01"
private const val MUL_CODE = "02"
private const val READ_CODE = "03"
private const val WRITE_CODE = "04"
private const val JPTR_CODE = "05"
private const val JPFA_CODE = "06"
private const val LESS_CODE = "07"
private const val EQ_CODE = "08"
private const val ADJ_BASE_CODE = "09"

private const val POSITION_MODE = '0'
private const val IMMEDIATE_MODE = '1'
private const val RELATIVE_MODE = '2'

class Computer(val intCode: IntCode) {
    private val intCodes = intCode.ints.mapIndexed{ i, value -> i.toLong() to value}.toMap().toMutableMap()
    private val inputs: Stack<Long> = Stack()
    val output = mutableListOf<Long>()

    private var pointer : Long = 0
    private var relativeBase : Long = 0

    fun run(input : Long = 0): List<Long> {
        inputs.push(input)
        while (pointer != -1L) {
            val execute = execute(pointer)
            if(execute == -2L) {
                // pause execution to wait for input
                return emptyList()
            } else {
                pointer = execute
            }
        }
        return intCodes.values.toList()
    }

    private fun execute(pointer: Long): Long {
        return executeInstruction(pointer)
    }

    private fun executeInstruction(
        pointer: Long
    ): Long {
        val instruction = intCodes[pointer].toString().leftPad(5, '0')
        return when (instruction.substring(3)) {
            END_CODE -> -1
            ADD_CODE -> executeAdd(instruction, pointer)
            MUL_CODE -> executeMultiply(instruction, pointer)
            READ_CODE -> executeRead(instruction, pointer)
            WRITE_CODE -> executeWrite(instruction, pointer)
            JPTR_CODE -> executeJumpIfTrue(instruction, pointer)
            JPFA_CODE -> executeJumpIfFalse(instruction, pointer)
            LESS_CODE -> executeLessThan(instruction, pointer)
            EQ_CODE -> executeEquals(instruction, pointer)
            ADJ_BASE_CODE -> executeAdjustRelativeBase(instruction, pointer)
            else -> throw IllegalStateException("Unknown instruction $instruction")
        }

    }

    private fun executeAdd(
        instruction: String,
        pointer: Long
    ): Long {
        val parameters = intCodes.subList(pointer + 1, pointer + 4)
        val targetIndex = getTargetIndex(parameters[2], instruction[0])
        intCodes[targetIndex] = calculateAdd(instruction, parameters)
        return pointer + 4
    }

    private fun getTargetIndex(param: Long, mode: Char): Long {
        return when(mode) {
            POSITION_MODE -> param
            RELATIVE_MODE -> param + relativeBase
            else -> throw IllegalStateException("$mode mode is not supported for intCode write operations ")
        }
    }


    private fun executeMultiply(
        instruction: String,
        pointer: Long
    ): Long {
        val parameters = intCodes.subList(pointer + 1, pointer + 4)
        val targetIndex = getTargetIndex(parameters[2], instruction[0])
        intCodes[targetIndex] = calculateMultiply(instruction, parameters)
        return pointer + 4
    }

    private fun executeRead(
        instruction: String,
        pointer: Long): Long {
        return if(inputs.empty()) {
            // pause execution
            -2
        } else {
            val targetIndex = getTargetIndex(intCodes[pointer + 1]?:0, instruction[2])
            intCodes[targetIndex] = inputs.pop()
            pointer + 2
        }
    }

    private fun executeWrite(
        instruction: String,
        pointer: Long
    ): Long {
        val parameter = intCodes[pointer + 1]?:0
        output.add(getParameterValue(instruction[2], parameter))
        return pointer + 2
    }

    private fun executeJumpIfTrue(instruction: String, pointer: Long): Long {
        val parameters = intCodes.subList(pointer + 1, pointer + 3)
        val (param1, param2) = getParameterValues(instruction, parameters)
        return if(param1 != 0L) {
            param2
        } else {
            // don't jump
            pointer + 3
        }
    }

    private fun executeJumpIfFalse(instruction: String, pointer: Long): Long {
        val parameters = intCodes.subList(pointer + 1, pointer + 3)
        val (param1, param2) = getParameterValues(instruction, parameters)
        return if(param1 == 0L) {
            param2
        } else {
            // don't jump
            pointer + 3
        }
    }

    private fun executeLessThan(instruction: String, pointer: Long): Long {
        val parameters = intCodes.subList(pointer + 1, pointer + 4)
        val targetIndex = getTargetIndex(parameters[2], instruction[0])
        intCodes[targetIndex] = calculateLessThan(instruction, parameters)
        return pointer + 4
    }

    private fun executeEquals(instruction: String, pointer: Long): Long {
        val parameters = intCodes.subList(pointer + 1, pointer + 4)
        val targetIndex = getTargetIndex(parameters[2], instruction[0])
        intCodes[targetIndex] = calculateEquals(instruction, parameters)
        return pointer + 4
    }

    private fun executeAdjustRelativeBase(instruction: String, pointer: Long): Long {
        relativeBase += getParameterValue(instruction[2], intCodes[pointer + 1]?:0)
        return pointer + 2
    }

    private fun calculateAdd(
        instruction: String,
        parameters: List<Long>
    ): Long {
        val (param1, param2) = getParameterValues(instruction, parameters)
        return param1 + param2
    }

    private fun calculateMultiply(
        instruction: String,
        parameters: List<Long>
    ): Long {
        val (param1, param2) = getParameterValues(instruction, parameters)
        return param1 * param2
    }

    private fun calculateLessThan(
        instruction: String,
        parameters: List<Long>
    ): Long {
        val (param1, param2) = getParameterValues(instruction, parameters)
        return if (param1 < param2) {
            1
        } else {
            0
        }
    }

    private fun calculateEquals (
        instruction: String,
        parameters: List<Long>
    ): Long {
        val (param1, param2) = getParameterValues(instruction, parameters)
        return if(param1 == param2) {
            1
        } else {
            0
        }
    }

    private fun getParameterValues(
        instruction: String,
        parameters: List<Long>
    ) : Pair<Long, Long> {
        return getParameterValue(instruction[2], parameters[0]) to
                getParameterValue(instruction[1], parameters[1])
    }

    private fun getParameterValue(
        mode: Char,
        parameter: Long
    ): Long {
        return when (mode) {
            IMMEDIATE_MODE -> parameter
            RELATIVE_MODE -> intCodes[relativeBase + parameter]?:0
            else -> intCodes[parameter]?:0
        }
    }

}

private fun MutableMap<Long, Long>.subList(startIndex: Long, endIndex: Long): List<Long> {
    return startIndex.until(endIndex).map { this[it]?:0 }
}

private fun String.leftPad(size: Long, char: Char): String {
    return 0.until(size - this.length).map { char }.joinToString("") + this
}
