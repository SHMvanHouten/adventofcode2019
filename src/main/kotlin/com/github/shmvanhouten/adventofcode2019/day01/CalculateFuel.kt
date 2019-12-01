package com.github.shmvanhouten.adventofcode2019.day01

fun calculateFuelSum(vararg masses: Int): Int {
    return calculateFuelSum(masses.toList())
}

fun calculateFuelSum(masses: String): Int {
    return calculateFuelSum(masses.split('\n').map { it.toInt() })
}

fun calculateFuelSum(masses: List<Int>): Int {
    return masses.map{ calculateFuel(it)}.sum()
}

fun calculateFuel(mass: Int): Int {
    return mass / 3 - 2
}
