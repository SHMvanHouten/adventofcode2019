package com.github.shmvanhouten.adventofcode2019.day17

import com.github.shmvanhouten.adventofcode2019.util.listPermutations

fun compressPathsToRoutines(path: List<String>): Routine {
    val functions = findViableFunctions(path)
    return findViableRoutine(functions, path)
}

fun findViableRoutine(
    functions: Set<List<String>>,
    path: List<String>
): Routine {
    return listPermutations(functions, maxSize = 3)
        .map { nameFunctions(it) }
        .mapNotNull { toRoutine(it, path) }
        .first()
}

fun nameFunctions(functions: List<List<String>>): Map<List<String>, Char> {
    return functions.mapIndexed { index, function ->
        function to 'A'.plus(index)
    }.toMap()
}

fun toRoutine(
    functions: Map<List<String>, Char>,
    path: List<String>
): Routine? {
    var remainingPath = path
    val mainRoutine = mutableListOf<Char>()
    while (remainingPath.isNotEmpty()) {
        val (function, name) = functions
            .filterKeys { it.size <= remainingPath.size }
            .filterKeys { remainingPath.subList(0, it.size) == it }
            .entries.maxBy { it.key.size }
            ?: return null
        mainRoutine += name
        remainingPath = remainingPath.subList(function.size, remainingPath.size)
    }
    return Routine(mainRoutine.joinToString(",") + "\n", toAsciiFunctions(functions))
}

fun toAsciiFunctions(functions: Map<List<String>, Char>): Map<Char, String> {
    return functions.map { (function, name) ->
        toAsciiFunction(name, function)
    }
        .toMap()
}

fun toAsciiFunction(name: Char, function: List<String>): Pair<Char, String> {
    return name to asciiRepresentation(function)
}

private fun asciiRepresentation(function: List<String>) =
    function.joinToString(",") { it } + "\n"

private fun findViableFunctions(path: List<String>): Set<List<String>> {
    return 0.until(path.size - 1)
        .map { path.subList(it, path.size) }
        .mapNotNull { findSequenceUnderSize20IfRecurring(it) }
        .toSet()
}

private fun findSequenceUnderSize20IfRecurring(it: List<String>): List<String>? {
    val index = generateSequence(1, Int::inc).takeWhile { i ->
        val subList = it.slice(0..i)
        doesNotExceed20Chars(subList) && subListRecurs(it, subList)
    }.lastOrNull()
    return if (index != null) {
        it.slice(0..index)
    } else {
        null
    }
}

private fun doesNotExceed20Chars(function: List<String>) =
    asciiRepresentation(function).length <= 20

private fun subListRecurs(path: List<String>, subList: List<String>): Boolean {
    return path.windowed(subList.size) {
        it == subList
    }.filter { it }.count() > 1
}

data class Routine(val main: String, val a: String, val b: String, val c: String) {
    constructor(main: String, functions: Map<Char, String>) : this(
        main, functions['A']!!, functions['B']!!, functions['C']!!
    )
}