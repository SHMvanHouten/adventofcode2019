package com.github.shmvanhouten.adventofcode2019.day14

import kotlin.math.ceil

class FuelMaker(chemicalReactions: List<Reaction>) {

    private val reactions = chemicalReactions.map { it.produces.chemical to it }.toMap()

    fun findAmountOfComponentsNeeded(amountOfFuel: Long): Long {

        var uncreatedChemicals = mutableListOf(ReactionComponent("FUEL", amountOfFuel))

        while (uncreatedChemicals.isNotEmpty()) {
            val (chemical, needed) = findAndRemoveFirstThatIsNotAReactorOfTheOthers(uncreatedChemicals)

            if (chemical == "ORE") {
                return needed

            } else {
                val (reactors, product) = reactions[chemical]!!
                val createdPerReaction = product.needed
                reactors
                    .map { chemicalToAmountNeeded(it, divideRoundedUp(needed, createdPerReaction)) }
                    .forEach { (reactor, amountNeeded) ->
                        uncreatedChemicals.add(ReactionComponent(reactor, amountNeeded))
                    }
            }
            uncreatedChemicals = uncreatedChemicals.mergeOnChemical()
        }

        throw IllegalStateException("No ORE found")
    }

    fun findAmountOfFuelThatCanBeMadeWith1Trillion(): Long {
        val target = 1000000000000L
        var maxInput = target
        var minInput = 1L
        while (true) {
            val fuelInput = (minInput + (maxInput - 1)) / 2
            val result = findAmountOfComponentsNeeded(fuelInput)
            when {
                result > target -> maxInput = fuelInput + 1
                fuelInput == minInput -> return fuelInput
                else -> minInput = fuelInput
            }
        }

    }

    private fun findAndRemoveFirstThatIsNotAReactorOfTheOthers(uncreatedChemicals: MutableList<ReactionComponent>): ReactionComponent {
        val first = uncreatedChemicals.first {
            it.isNotReactorOf(uncreatedChemicals, reactions)
        }
        uncreatedChemicals.remove(first)
        return first
    }

    private fun chemicalToAmountNeeded(reactor: ReactionComponent, multiplicant: Long): Pair<Chemical, Long> {
        return reactor.chemical to reactor.needed * multiplicant
    }

    private fun divideRoundedUp(actuallyNeeded: Long, createdPerReaction: Long) =
        ceil(actuallyNeeded / createdPerReaction.toDouble()).toLong()

}

private fun MutableList<ReactionComponent>.mergeOnChemical(): MutableList<ReactionComponent> {
    val chemicalToAmount = this.map { it.chemical to 0L }.toMap().toMutableMap()
    this.forEach { component ->
        chemicalToAmount.merge(component.chemical, component.needed) { t, u -> t + u }
    }
    return chemicalToAmount.entries.map { ReactionComponent(it.key, it.value.toLong()) }.toMutableList()
}
