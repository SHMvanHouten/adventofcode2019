package com.github.shmvanhouten.adventofcode2019.util

fun <T> listPermutations(nodes: Collection<T>, maxSize: Int = nodes.size) =
    listPermutations(nodes, maxSize, emptyList(), emptyList())

private fun <T> listPermutations(
    nodes: Collection<T>,
    maxSize: Int,
    routes: List<List<T>>,
    currentRoute: List<T>
): List<List<T>> {
    return if (currentRoute.size == maxSize) {
        routes.plus(listOf(currentRoute))
    } else {
        nodes.flatMap { node -> listPermutations(nodes.minus(node), maxSize, routes, currentRoute.plus(node)) }
    }
}
