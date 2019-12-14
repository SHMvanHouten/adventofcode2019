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

data class ReactionComponent(val chemical: Chemical, val needed: Long) {

    private fun flatMapReactors(reactions: Map<Chemical, Reaction>): MutableList<ReactionComponent> {
        val reaction = reactions[this.chemical] ?: return mutableListOf()
        val reactors = reaction.reactors.toMutableList()
        var lastAddedReactors = reactors
            .mapNotNull { reactions[it.chemical] }
            .flatMap { it.reactors }
        while(lastAddedReactors.isNotEmpty()) {
            reactors += lastAddedReactors
            lastAddedReactors = lastAddedReactors
                .mapNotNull { reactions[it.chemical] }
                .flatMap { it.reactors }
        }
        return reactors
    }

    fun isNotReactorOf(
        uncreatedChemicals: MutableList<ReactionComponent>,
        reactions: Map<Chemical, Reaction>
    ): Boolean {
        return (uncreatedChemicals - this)
            .none {
                it.flatMapReactors(reactions)
                    .map { r -> r.chemical }
                    .contains(this.chemical)
            }
    }
}



