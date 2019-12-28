package com.github.shmvanhouten.adventofcode2019.util

import java.math.BigInteger

fun findNeededPowersOf2ToGetTarget(
    target: BigInteger
): List<Int> {
    val twosToPowers = powerOf2sUntilN(target)
    var remainder = target
    val neededPowers = mutableListOf<Int>()
    while (remainder > BigInteger.ZERO) {
        val (power, result) = findMaximumPowerOfTwo(remainder, twosToPowers)
        neededPowers += power
        remainder -= result
    }
    return neededPowers
}

private fun findMaximumPowerOfTwo(remainder: BigInteger, twosToPowers: List<Pair<Int, BigInteger>>): Pair<Int, BigInteger> {
    return twosToPowers.filter { remainder - it.second >= BigInteger.ZERO }
        .maxBy { it.first } ?: throw IllegalStateException("could not find any powers for $remainder, powers: $twosToPowers")
}

fun powerOf2sUntilN(n: BigInteger): List<Pair<Int, BigInteger>> {
    return generateSequence(0, Int::inc)
        .map { it to BigInteger.valueOf(2).pow(it) }
        .takeWhile { n - it.second >= BigInteger.ZERO }
        .toList()
}