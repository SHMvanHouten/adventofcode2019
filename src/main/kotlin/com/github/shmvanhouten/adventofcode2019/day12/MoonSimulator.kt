package com.github.shmvanhouten.adventofcode2019.day12

import com.github.shmvanhouten.adventofcode2019.leastcommonmultiple.leastCommonMultiple
import kotlin.math.abs

// PART 2
fun countStepsToDuplicateState(moons: List<Moon>): Long {
    val stepsInXPattern = getPattern(moons.map { it.position.x to it.velocity.x }).size
    val stepsInYPattern = getPattern(moons.map { it.position.y to it.velocity.y }).size
    val stepsInZPattern = getPattern(moons.map { it.position.z to it.velocity.z }).size
    return leastCommonMultiple(
        listOf(
            stepsInXPattern.toLong(),
            stepsInYPattern.toLong(),
            stepsInZPattern.toLong()
        )
    )
}

fun getPattern(velocityToPositionPairs: MoonDimensions): MutableSet<MoonDimensions> {
    val states = mutableSetOf<MoonDimensions>()
    var latestState = velocityToPositionPairs

    while (!states.contains(latestState)) {
        states.add(latestState)
        latestState = step(latestState)
    }
    return states
}

fun step(moonDimensions: MoonDimensions): List<Pair<Int, Int>> {
    return moonDimensions.map { step(it, moonDimensions - it) }
}

fun step(dimensionToVelocity: Pair<Int, Int>, others: MoonDimensions): Pair<Int, Int> {
    val position = dimensionToVelocity.first
    val newVelocity = dimensionToVelocity.second + count(position,others.map { it.first })
    return position + newVelocity to newVelocity
}
 // PART 1
fun tick(constellation: List<Moon>): List<Moon> {
    return constellation.map { moon ->
        tick(moon, constellation - moon)
    }
}

fun calculateEnergy(moons: List<Moon>): Long {
    return moons
        .map { calculateEnergy(it) }
        .sum()
}

private fun calculateEnergy(moon: Moon): Long {
    return calculateEnergy(moon.position) * calculateEnergy(moon.velocity)
}

private fun calculateEnergy(coord3d: Coord3d): Long {
    return abs(coord3d.x).toLong() + abs(coord3d.y) + abs(coord3d.z)
}

private fun tick(moon: Moon, others: List<Moon>): Moon {
    val newVelocity = moon.velocity + Coord3d(countx(others, moon), county(others, moon), countz(others, moon))
    return Moon(moon.position + newVelocity, newVelocity)
}

private fun countx(others: List<Moon>, moon: Moon): Int {
    return count(moon.position.x, others.map { it.position }.map { it.x })
}

private fun county(others: List<Moon>, moon: Moon): Int {
    return count(moon.position.y, others.map { it.position }.map { it.y })
}

private fun countz(others: List<Moon>, moon: Moon): Int {
    return count(moon.position.z, others.map { it.position }.map { it.z })
}

private fun count(
    dimension: Int,
    dimensions: List<Int>
): Int {
    val less = dimensions.count { it < dimension }
    val more = dimensions.count { it > dimension }
    return more - less
}

typealias MoonDimensions = List<Pair<Int, Int>>
