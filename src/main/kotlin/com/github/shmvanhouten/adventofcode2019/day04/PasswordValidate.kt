package com.github.shmvanhouten.adventofcode2019.day04

fun isValid(password: Int): Boolean {
    val pw = password.toString().toList()
    return passwordDoesNotDecrease(pw) && passwordHasRepeatingDigit(pw)
}

private fun passwordDoesNotDecrease(pw: List<Char>) = pw.sorted() == pw

fun passwordHasRepeatingDigit(pw: List<Char>): Boolean {
    return pw.any { c -> pw.count { c == it } >= 2 }
}

fun isValid2(password: Int): Boolean {
    val pw = password.toString().toList()
    return passwordDoesNotDecrease(pw) && passwordHasRepeatingDigitNotPartOfGroup(pw)
}

fun passwordHasRepeatingDigitNotPartOfGroup(pw: List<Char>): Boolean {
    return pw.any { c -> pw.count { c == it } == 2 }
}

fun countValidPasswordsInRange(intRange: IntRange): Int {
    return intRange.count { isValid(it) }
}

fun countValidPasswordsInRange2(intRange: IntRange): Int {
    return intRange.count { isValid2(it) }
}
