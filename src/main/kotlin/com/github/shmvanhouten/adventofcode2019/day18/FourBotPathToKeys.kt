package com.github.shmvanhouten.adventofcode2019.day18

fun stepsRequiredBy4Bots(input: String): Int {
    val fourMapStates = breakUpFourMaps(input)

    val unfinishedRoutes = mutableSetOf(FourWayRoute(listOf(listOf('@'), listOf('@'), listOf('@'), listOf('@')), 0))
    var fastestRoute: FourWayRoute? = null

    while (unfinishedRoutes.isNotEmpty()) {
        val route = unfinishedRoutes.minBy { it.length }!!
        unfinishedRoutes.remove(route)
        if (allKeysHaveBeenCollected(fourMapStates, route)) {
            fastestRoute = route
        } else {
            val collectedKeys = route.paths.flatMap { getKeys(it) }
            unfinishedRoutes += 0.until(4)
                .map { i -> findAvailableNextSteps(i, collectedKeys, fourMapStates, route) }.flatten()
        }

        unfinishedRoutes.removeIf { it.length >= fastestRoute?.length ?: Int.MAX_VALUE }
    }
    return fastestRoute?.length ?: throw Exception("No route found")
}

fun findAvailableNextSteps(
    i: Int,
    collectedKeys: List<Char>,
    fourMapStates: List<KeysToVectorBetweenKeys>,
    route: FourWayRoute
): List<FourWayRoute> {
    val currentBotPath = route.paths[i]
    val (keys, currentMap) = fourMapStates[i]
    val uncollectedKeys = keys - getKeys(currentBotPath)
    return uncollectedKeys
        .asSequence()
        .map { it to findVector(currentBotPath.last(), it, currentMap) }
        .filter { hasKeysForAllDoors(it.second, collectedKeys) }
        .filter { allKeysOnNewPathWereAlreadyCollected(it, currentBotPath) }
        .map { (key, vector) -> Route(currentBotPath + key, vector.path.size + route.length) }
        .map { route.withNewRouteAtIndex(it, i) }
        .toList()

}

private fun allKeysOnNewPathWereAlreadyCollected(
    it: Pair<Char, Vector>,
    currentBotPath: List<Char>
) = (it.second.keys - it.first).all { key -> getKeys(currentBotPath).contains(key) }


fun allKeysHaveBeenCollected(fourMapStates: List<KeysToVectorBetweenKeys>, route: FourWayRoute): Boolean {
    return route.paths.mapIndexed { i, path -> fourMapStates[i].first - path }.all { it.isEmpty() }
}

data class FourWayRoute(
    val paths: List<List<Char>>,
    val length: Int
) {
    fun withNewRouteAtIndex(route: Route, i: Int): FourWayRoute {
        val allRoutes = paths.toMutableList()
        allRoutes[i] = route.path
        return FourWayRoute(allRoutes, route.length)
    }
}