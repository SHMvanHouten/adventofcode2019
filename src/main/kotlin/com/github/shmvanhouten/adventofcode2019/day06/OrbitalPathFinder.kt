package com.github.shmvanhouten.adventofcode2019.day06

val YOU = "YOU"
val SAN = "SAN"

fun shortestPath(input: String): Int {
    val planets = toPlanets(input) + Planet("COM", "NOTHING")
    val visitedPlanets = mutableMapOf<Planet, Int>()
    val unvisitedPlanets = mutableListOf(PlanetNode(findStartingPoint(planets), -1))

    while (unvisitedPlanets.isNotEmpty()) {
        val planetNode = unvisitedPlanets.first()
        if(visitedPlanets.none { it.key == planetNode.planet }) {
            val pathSize = planetNode.pathSize
            val adjacentPlanets = findAdjacentPlanets(planets, planetNode)
                .filter { !visitedPlanets.contains(it) }

            if(adjacentPlanets.any { it.name == SAN }) {
                return pathSize
            }

            visitedPlanets[planetNode.planet] = pathSize
            unvisitedPlanets += adjacentPlanets.map { PlanetNode(it, pathSize + 1) }
            unvisitedPlanets.sort()
        }
        unvisitedPlanets.remove(planetNode)
    }
    return -1
}

private fun findAdjacentPlanets(
    planets: Set<Planet>,
    planetNode: PlanetNode
) = planets.filter { it.orbits(planetNode.planet.name) } +
        planets.filter { planetNode.planet.orbits(it.name) }

data class PlanetNode(val planet: Planet, val pathSize: Int): Comparable<PlanetNode> {
    override fun compareTo(other: PlanetNode): Int {
        return this.pathSize.compareTo(other.pathSize)
    }
}
private fun findStartingPoint(
    planets: Set<Planet>
) =
    planets.find { it.name == YOU } ?: throw IllegalStateException()