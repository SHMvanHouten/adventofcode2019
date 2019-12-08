package com.github.shmvanhouten.adventofcode2019.util

fun <T> listPermutations(nodes: List<T>) =
    listPermutations(nodes, emptyList(), emptyList())

fun <T> listPermutations(
    nodes: List<T>,
    routes: List<List<T>>,
    currentRoute: List<T>
): List<List<T>> {
    return if (atEndOfRoute(nodes)) {
        routes.plus(listOf(currentRoute))
    } else {
        nodes.flatMap { node -> listPermutations(nodes.minus(node), routes, currentRoute.plus(node)) }
    }
}

private fun <T> atEndOfRoute(nodes: List<T>) = nodes.isEmpty()