package com.github.shmvanhouten.adventofcode2019.primefactor

fun primeFactors(number: Long): List<Long> {
    val factors = mutableListOf<Long>()
    var remainder = number

    var divisor = 2L
    while (remainder > 1) {
        while (remainder % divisor == 0L) {
            factors.add(divisor)
            remainder /= divisor
        }
        divisor++
    }
    if(remainder > 1) {
        factors.add(remainder)
    }
    return factors
}