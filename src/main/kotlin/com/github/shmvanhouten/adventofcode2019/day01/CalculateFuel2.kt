package com.github.shmvanhouten.adventofcode2019.day01

fun calculateCumulativeFuelSum(vararg masses: Int): Int {
    masses.map { generateSequence {  } }
    return calculateCumulativeFuelSum(masses.toList())
}

fun calculateCumulativeFuelSum(masses: String): Int {
    return calculateCumulativeFuelSum(masses.split('\n').map { it.toInt() })
}

fun calculateCumulativeFuelSum(masses: List<Int>): Int {
    return masses
        .map{ calculateCumulativeFuel(it)}
        .sum()
}

fun calculateCumulativeFuel(mass: Int): Int {
    var fuel = 0
    var remainingMass = calculateFuel(mass)
    while (remainingMass > 0) {
        fuel += remainingMass
        remainingMass = calculateFuel(remainingMass)
    }
    return fuel
}
