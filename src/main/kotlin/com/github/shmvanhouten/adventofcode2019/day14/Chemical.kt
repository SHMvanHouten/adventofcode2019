package com.github.shmvanhouten.adventofcode2019.day14

import com.github.shmvanhouten.adventofcode2017.util.splitIntoTwo

typealias Chemical = String

fun String.toReactions(): List<Reaction> {
    return this.split('\n')
        .map { toReaction(it) }
}

fun toReaction(raw: String): Reaction {
    val (rawTakes, rawProduces) = raw.splitIntoTwo(" => ")
    val takes = toReactionComponents(rawTakes)
    val produces = toReactionComponent(rawProduces)
    return Reaction(takes, produces)
}

private fun toReactionComponents(rawTakes: String) = rawTakes.split(", ").map { toReactionComponent(it) }

fun toReactionComponent(rawComponent: String): ReactionComponent {
    val (amount, chemical) = rawComponent.splitIntoTwo(" ")
    return ReactionComponent(chemical, amount.toLong())
}

data class Reaction(val reactors: List<ReactionComponent>, val produces: ReactionComponent)

data class ReactionComponent(val chemical: Chemical, val amountPerReaction: Long) {

    fun isNotReactorOf(
        chemicals: List<ReactionComponent>,
        reactions: Map<Chemical, Reaction>
    ): Boolean =
        chemicals.none {
            it.flatMapReactors(reactions)
                .map { r -> r.chemical }
                .contains(this.chemical)
        }

    private fun flatMapReactors(reactions: Map<Chemical, Reaction>): MutableList<ReactionComponent> {
        var nextReactors = reactions[this.chemical]?.reactors?: return mutableListOf()
        val reactors = nextReactors.toMutableList()
        while (nextReactors.isNotEmpty()) {
            reactors += nextReactors
            nextReactors = findAllReactorsForReactors(nextReactors, reactions)
        }
        return reactors
    }

    private fun findAllReactorsForReactors(
        reactors: List<ReactionComponent>,
        reactions: Map<Chemical, Reaction>
    ): List<ReactionComponent> =
        reactors
            .mapNotNull { reactions[it.chemical] }
            .flatMap { it.reactors }
}



