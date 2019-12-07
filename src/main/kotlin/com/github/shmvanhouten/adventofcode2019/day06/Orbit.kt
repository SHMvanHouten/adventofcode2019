package com.github.shmvanhouten.adventofcode2019.day06

import com.github.shmvanhouten.adventofcode2017.util.splitIntoTwo

fun countOrbits(input: String): Int {
    val planets = toPlanets(input)
    var orbits = planets.filter { it.orbits("COM") }
    var orbitValue = 1
    var orbitsTotal = orbits.count() * orbitValue
    while (orbits.isNotEmpty()) {
        orbits = planets.filter { it.orbitsAnyOf(orbits) }
        orbitsTotal += orbits.count() * ++orbitValue
    }
    return orbitsTotal
}

fun toPlanets(input: String) = input.split('\n').map { toPlanet(it) }.toSet()

fun toPlanet(orbitMap: String): Planet {
    val (center, orbitingBody) = orbitMap.splitIntoTwo(")")
    return Planet(orbitingBody, center)
}

data class Planet(val name: String, val orbitsAround: String) {
    fun orbitsAnyOf(planets: List<Planet>): Boolean {
        return planets.any { this.orbits(it.name) }
    }

    fun orbits(planet: String): Boolean {
        return orbitsAround == planet
    }
}
