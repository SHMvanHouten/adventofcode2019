package com.github.shmvanhouten.adventofcode2019.day15

import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Coordinate
import com.github.shmvanhouten.adventofcode2017.day03spiralmemory.Direction

class PathFinder {

    fun findPathAndMap(startingDroid: RepairDroid): Pair<List<Coordinate>, MutableSet<Coordinate>> {
        val unvisitedNodes = mutableListOf(Node(startingDroid, listOf()))
        val visitedLocations = mutableSetOf<Coordinate>()
        var pathToTarget = listOf<Coordinate>()

        while (unvisitedNodes.isNotEmpty()) {
            unvisitedNodes.sort()
            val node = unvisitedNodes.removeAt(0)
            val droid = node.droid
            visitedLocations.add(droid.location)

            if(node.hasOxygenMachine) {
                pathToTarget = node.path
            }

            val newNodes = moveToNewLocations(droid, visitedLocations, node.path)
            unvisitedNodes.addAll(newNodes)
        }

        check(pathToTarget.isNotEmpty()) { "Target not found" }
        return pathToTarget to visitedLocations
    }

    private fun moveToNewLocations(
        droid: RepairDroid,
        visitedLocations: MutableSet<Coordinate>,
        path: List<Coordinate>
    ): List<Node> {
        return Direction.values()
            .mapNotNull { droid.move(it) }
            .filter { locationWasNotVisited(visitedLocations, it.second) }
            .map { toNode(it, path) }
    }

    private fun toNode(
        it: Pair<Int, RepairDroid>,
        path: List<Coordinate>
    ) = Node(it.second, path + it.second.location, it.first == 2)

    private fun locationWasNotVisited(
        visitedLocations: MutableSet<Coordinate>,
        droid: RepairDroid
    ) = visitedLocations.none { droid.location == it }
}

data class Node(
    val droid: RepairDroid,
    val path: List<Coordinate>,
    val hasOxygenMachine: Boolean = false
) : Comparable<Node> {

    override fun compareTo(other: Node): Int {
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
