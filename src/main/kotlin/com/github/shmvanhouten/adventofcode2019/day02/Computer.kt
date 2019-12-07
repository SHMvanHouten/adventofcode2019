package com.github.shmvanhouten.adventofcode2019.day02

private const val END_CODE = "99"
private const val ADD_CODE = "01"
private const val MUL_CODE = "02"
private const val READ_CODE = "03"
private const val WRITE_CODE = "04"
private const val JPTR_CODE = "05"
private const val JPFA_CODE = "06"
private const val LESS_CODE = "07"
private const val EQ_CODE = "08"


class Computer(private val input: Int = 0, val intCode: IntCode) {
    private val intCodes = intCode.ints.toMutableList()
    val output = mutableListOf<Int>()

    fun run(): List<Int> {
        var pointer = 0
        while (pointer != -1) {
            pointer = execute(pointer)
        }
        return intCodes
    }

    private fun execute(pointer: Int): Int {
        return executeInstruction(intCodes, pointer)
    }

    private fun executeInstruction(
        intCodes: MutableList<Int>,
        pointer: Int
    ): Int {
        val instruction = intCodes[pointer].toString().leftPad(5, '0')
        return when (instruction.substring(3)) {
            END_CODE -> -1
            ADD_CODE -> executeAdd(instruction, intCodes, pointer)
            MUL_CODE -> executeMultiply(instruction, intCodes, pointer)
            READ_CODE -> executeRead(intCodes, pointer)
            WRITE_CODE -> executeWrite(instruction, intCodes, pointer)
            JPTR_CODE -> executeJumpIfTrue(instruction, intCodes, pointer)
            JPFA_CODE -> executeJumpIfFalse(instruction, intCodes, pointer)
            LESS_CODE -> executeLessThan(instruction, intCodes, pointer)
            EQ_CODE -> executeEquals(instruction, intCodes, pointer)
            else -> throw IllegalStateException("Unknown instruction $instruction")
        }

    }

    private fun executeAdd(
        instruction: String,
        intCodes: MutableList<Int>,
        pointer: Int
    ): Int {
        val parameters = intCodes.subList(pointer + 1, pointer + 4)
        intCodes[parameters[2]] = calculateAdd(instruction, intCodes, parameters)
        return pointer + 4
    }

    private fun executeMultiply(
        instruction: String,
        intCodes: MutableList<Int>,
        pointer: Int
    ): Int {
        val parameters = intCodes.subList(pointer + 1, pointer + 4)
        intCodes[parameters[2]] = calculateMultiply(instruction, intCodes, parameters)
        return pointer + 4
    }

    private fun executeRead(intCodes: MutableList<Int>, pointer: Int): Int {
        intCodes[intCodes[pointer + 1]] = input
        return pointer + 2
    }

    private fun executeWrite(
        instruction: String,
        intCodes: MutableList<Int>,
        pointer: Int
    ): Int {
        if (instruction[2] == '1') {
            output.add(intCodes[pointer + 1])
        } else {
            output.add(intCodes[intCodes[pointer + 1]])
        }
        return pointer + 2
    }

    private fun executeJumpIfTrue(instruction: String, intCodes: MutableList<Int>, pointer: Int): Int {
        val parameters = intCodes.subList(pointer + 1, pointer + 3)
        val (param1, param2) = getParameterValues(instruction, intCodes, parameters)
        return if(param1 != 0) {
            param2
        } else {
            pointer + 3
        }
    }

    private fun executeJumpIfFalse(instruction: String, intCodes: MutableList<Int>, pointer: Int): Int {
        val parameters = intCodes.subList(pointer + 1, pointer + 3)
        val (param1, param2) = getParameterValues(instruction, intCodes, parameters)
        return if(param1 == 0) {
            param2
        } else {
            pointer + 3
        }
    }

    private fun executeLessThan(instruction: String, intCodes: MutableList<Int>, pointer: Int): Int {
        val parameters = intCodes.subList(pointer + 1, pointer + 4)
        intCodes[parameters[2]] = calculateLessThan(instruction, intCodes, parameters)
        return pointer + 4
    }

    private fun executeEquals(instruction: String, intCodes: MutableList<Int>, pointer: Int): Int {
        val parameters = intCodes.subList(pointer + 1, pointer + 4)
        intCodes[parameters[2]] = calculateEquals(instruction, intCodes, parameters)
        return pointer + 4
    }

    private fun calculateAdd(
        instruction: String,
        intCodes: MutableList<Int>,
        parameters: MutableList<Int>
    ): Int {
        val (param1, param2) = getParameterValues(instruction, intCodes, parameters)
        return param1 + param2
    }

    private fun calculateMultiply(
        instruction: String,
        intCodes: MutableList<Int>,
        parameters: MutableList<Int>
    ): Int {
        val (param1, param2) = getParameterValues(instruction, intCodes, parameters)
        return param1 * param2
    }

    private fun calculateLessThan(
        instruction: String,
        intCodes: MutableList<Int>,
        parameters: MutableList<Int>
    ): Int {
        val (param1, param2) = getParameterValues(instruction, intCodes, parameters)
        return if (param1 < param2) {
            1
        } else {
            0
        }
    }

    private fun calculateEquals (
        instruction: String,
        intCodes: MutableList<Int>,
        parameters: MutableList<Int>
    ): Int {
        val (param1, param2) = getParameterValues(instruction, intCodes, parameters)
        return if(param1 == param2) {
            1
        } else {
            0
        }
    }

    private fun getParameterValue(
        mode: Char,
        intCodes: MutableList<Int>,
        parameter: Int
    ): Int {
        return if (mode == '0') {
            intCodes[parameter]
        } else {
            parameter
        }
    }

    private fun getParameterValues(
        instruction: String,
        intCodes: MutableList<Int>,
        parameters: MutableList<Int>
    ) : Pair<Int, Int> {
        return getParameterValue(instruction[2], intCodes, parameters[0]) to
                getParameterValue(instruction[1], intCodes, parameters[1])
    }

}

private fun String.leftPad(size: Int, char: Char): String {
    return 0.until(size - this.length).map { char }.joinToString("") + this
}
