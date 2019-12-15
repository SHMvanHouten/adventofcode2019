package com.github.shmvanhouten.adventofcode2019.day15

import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Coordinate
import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Direction

class PathFinder {

    fun findPathAndMap(startingDroid: RepairDroid): Pair<List<Coordinate>, MutableSet<Coordinate>> {
        val unvisitedNodes = mutableListOf(DroidNode(startingDroid, listOf()))
        val visitedLocations = mutableSetOf<Coordinate>()
        var pathToTarget = listOf<Coordinate>()

        while (unvisitedNodes.isNotEmpty()) {
            unvisitedNodes.sort()
            val first = unvisitedNodes.first()
            unvisitedNodes.remove(first)
            visitedLocations.add(first.droid.location)

            val (droid, path) = first

            if(pathToTarget.isEmpty()) {
                val targetLocation = findTargetLocation(droid)
                if (targetLocation != null) {
                    pathToTarget = path + targetLocation
                }
            }

            val newNodes = Direction.values()
                .map { droid.move(it) }
                .mapNotNull { it.second }
                .filter { locationWasNotVisited(visitedLocations, it) }
                .map { DroidNode(it, path + it.location) }

            unvisitedNodes.addAll(newNodes)
        }

        check(pathToTarget.isNotEmpty()) { "Target not found" }
        return pathToTarget to visitedLocations
    }

    private fun findTargetLocation(droid: RepairDroid): Coordinate? {
        return Direction.values()
            .map { droid.move(it) }
            .find { it.first == 2 }?.second?.location
    }

    private fun locationWasNotVisited(
        visitedLocations: MutableSet<Coordinate>,
        droid: RepairDroid
    ) = visitedLocations.none { droid.location == it }
}

data class DroidNode(val droid: RepairDroid, val path: List<Coordinate>) : Comparable<DroidNode> {
    override fun compareTo(other: DroidNode): Int {
        if (this.path.size == other.path.size) {
            return this.droid.location.compareTo(other.droid.location)
        }
        return this.path.size.compareTo(other.path.size)
    }
}

private fun Coordinate.compareTo(other: Coordinate): Int {
    return if (this.y == other.y) {
        this.x.compareTo(other.x)
    } else {
        this.y.compareTo(other.y)
    }
}
