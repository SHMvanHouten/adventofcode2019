package com.github.shmvanhouten.adventofcode2019.day02

import com.github.shmvanhouten.adventofcode2019.day02.ExecutionType.*
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

interface IComputer {
    val output: Queue<Long>

    fun input(input: Long = 0): ExecutionType
    fun run(): ExecutionType
    fun copy(): IComputer
}

class Computer(
    val intCode: IntCode,
    private var pointer: Long = 0,
    private var relativeBase: Long = 0,
    private var execution: ExecutionType = RUN,
    private val inputs: Queue<Long> = LinkedList(),
    override val output: Queue<Long> = LinkedList<Long>()
) : IComputer {

    private val intCodes = intCode.ints.toMutableMap()

    override fun copy(): Computer {
        val intCode = IntCode(intCodes)
        return Computer(
            intCode,
            this.pointer,
            this.relativeBase,
            this.execution,
            this.inputs,
            this.output
        )
    }

    override fun input(input: Long): ExecutionType {
        inputs.add(input)
        execution = RUN
        while (execution == RUN) {
            pointer = execute(pointer)
        }
        return execution
    }

    override fun run(): ExecutionType {
        execution = RUN
        while (execution == RUN) {
            pointer = execute(pointer)
        }
        return execution
    }

    fun getIntCodes(): List<Long> {
        return intCodes.values.toList()
    }

    private fun execute(pointer: Pointer): Pointer {
        return executeInstruction(pointer)
    }

    private fun executeInstruction(pointer: Pointer): Pointer {
        val instruction = toInstruction(pointer)
        return when (instruction.opCode()) {
            END_CODE -> end(pointer)
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

    private fun end(pointer: Pointer): Pointer {
        execution = STOP
        return pointer
    }

    private fun executeAdd(instruction: Instruction, pointer: Pointer): Pointer {
        val parameters = intCodes.subList(pointer + 1, pointer + 4)
        val targetIndex = getTargetIndex(parameters[2], instruction.thirdParameterMode())
        intCodes[targetIndex] = calculateAdd(instruction, parameters)
        return pointer + 4
    }

    private fun executeMultiply(instruction: Instruction, pointer: Pointer): Pointer {
        val parameters = intCodes.subList(pointer + 1, pointer + 4)
        val targetIndex = getTargetIndex(parameters[2], instruction.thirdParameterMode())
        intCodes[targetIndex] = calculateMultiply(instruction, parameters)
        return pointer + 4
    }

    private fun executeRead(instruction: Instruction, pointer: Pointer): Pointer {
        return if (inputs.isEmpty()) {
            execution = REQUIRES_INPUT
            pointer
        } else {
            val targetIndex = getTargetIndex(intCodes[pointer + 1] ?: 0, instruction.firstParameterMode())
            intCodes[targetIndex] = inputs.poll()
            pointer + 2
        }
    }

    private fun executeWrite(instruction: Instruction, pointer: Pointer): Pointer {
        val parameter = intCodes[pointer + 1] ?: 0
        output.add(getParameterValue(instruction.firstParameterMode(), parameter))
        return pointer + 2
    }

    private fun executeJumpIfTrue(instruction: Instruction, pointer: Pointer): Pointer {
        val parameters = intCodes.subList(pointer + 1, pointer + 3)
        val (param1, param2) = getParameterValues(instruction, parameters[0], parameters[1])
        return if (param1 != 0L) {
            param2
        } else {
            // don't jump
            pointer + 3
        }
    }

    private fun executeJumpIfFalse(instruction: Instruction, pointer: Pointer): Pointer {
        val parameters = intCodes.subList(pointer + 1, pointer + 3)
        val (param1, param2) = getParameterValues(instruction, parameters[0], parameters[1])
        return if (param1 == 0L) {
            param2
        } else {
            // don't jump
            pointer + 3
        }
    }

    private fun executeLessThan(instruction: Instruction, pointer: Pointer): Pointer {
        val parameters = intCodes.subList(pointer + 1, pointer + 4)
        val targetIndex = getTargetIndex(parameters[2], instruction.thirdParameterMode())
        intCodes[targetIndex] = calculateLessThan(instruction, parameters)
        return pointer + 4
    }

    private fun executeEquals(instruction: Instruction, pointer: Pointer): Pointer {
        val parameters = intCodes.subList(pointer + 1, pointer + 4)
        val targetIndex = getTargetIndex(parameters[2], instruction.thirdParameterMode())
        intCodes[targetIndex] = calculateEquals(instruction, parameters)
        return pointer + 4
    }

    private fun executeAdjustRelativeBase(instruction: Instruction, pointer: Pointer): Pointer {
        relativeBase += getParameterValue(instruction.firstParameterMode(), intCodes[pointer + 1] ?: 0)
        return pointer + 2
    }

    private fun calculateAdd(instruction: Instruction, parameters: List<Long>): Long =
        calculate(instruction, parameters) { i, j -> i + j }

    private fun calculateMultiply(instruction: Instruction, parameters: List<Long>): Long =
        calculate(instruction, parameters) { i, j -> i * j }

    private fun calculate(
        instruction: Instruction,
        parameters: List<Long>,
        operation: (Long, Long) -> Long
    ): Long {
        val (param1, param2) = getParameterValues(instruction, parameters[0], parameters[1])
        return operation.invoke(param1, param2)
    }

    private fun calculateLessThan(instruction: Instruction, parameters: List<Long>): Long {
        val (param1, param2) = getParameterValues(instruction, parameters[0], parameters[1])
        return if (param1 < param2) {
            1
        } else {
            0
        }
    }

    private fun calculateEquals(instruction: Instruction, parameters: List<Long>): Long {
        val (param1, param2) = getParameterValues(instruction, parameters[0], parameters[1])
        return if (param1 == param2) {
            1
        } else {
            0
        }
    }

    private fun getParameterValues(
        instruction: Instruction,
        firstParameter: Long,
        secondParameter: Long
    ): Pair<Long, Long> = getParameterValue(instruction.firstParameterMode(), firstParameter) to
            getParameterValue(instruction.secondParameterMode(), secondParameter)

    private fun getParameterValue(mode: Char, parameter: Long): Long =
        when (mode) {
            IMMEDIATE_MODE -> parameter
            RELATIVE_MODE -> intCodes[relativeBase + parameter] ?: 0
            POSITION_MODE -> intCodes[parameter] ?: 0
            else -> throw IllegalStateException("$mode mode is not supported")
        }

    private fun getTargetIndex(param: Long, mode: Char): Long =
        when (mode) {
            POSITION_MODE -> param
            RELATIVE_MODE -> param + relativeBase
            else -> throw IllegalStateException("$mode mode is not supported for intCode write operations ")
        }

    private fun toInstruction(pointer: Long): Instruction =
        intCodes[pointer].toString().leftPad(5, '0')

}

private fun String.secondParameterMode(): Mode {
    return this[1]
}

private fun String.firstParameterMode(): Mode {
    return this[2]
}

private fun String.thirdParameterMode(): Mode {
    return this[0]
}

private fun String.opCode(): OpCode {
    return substring(3)
}

private fun MutableMap<Long, Long>.subList(startIndex: Long, endIndex: Long): List<Long> {
    return startIndex.until(endIndex).map { this[it] ?: 0 }
}

private fun String.leftPad(size: Long, char: Char): String {
    return 0.until(size - this.length).map { char }.joinToString("") + this
}

typealias Instruction = String
typealias OpCode = String
typealias Mode = Char
typealias Pointer = Long

enum class ExecutionType {
    RUN,
    REQUIRES_INPUT,
    STOP
}
