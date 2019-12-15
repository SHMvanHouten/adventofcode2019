package com.github.shmvanhouten.adventofcode2019.day14

import kotlin.math.ceil

class FuelMaker(chemicalReactions: List<Reaction>) {

    private val reactions = chemicalReactions.map { it.produces.chemical to it }.toMap()

    fun findAmountOfComponentsNeeded(amountOfFuel: Long): Long {

        var uncreatedChemicals = mutableListOf(ReactionComponent("FUEL", amountOfFuel))

        while (uncreatedChemicals.isNotEmpty()) {
            uncreatedChemicals = uncreatedChemicals.mergeOnChemical()
            val (chemical, amountRequired) = takeFirstThatIsNotAReactorOfTheRest(uncreatedChemicals)

            if (chemical == "ORE") {
                return amountRequired
            } else {
                uncreatedChemicals.addAll(
                    findAllReactorsForProduct(chemical, amountRequired)
                )
            }
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

    private fun findAllReactorsForProduct(chemical: Chemical, amountRequired: Long): List<ReactionComponent> {
        val (reactors, product) = reactions[chemical]!!
        return reactors.map { toReactionComponent(it, amountRequired, product.amountPerReaction) }
    }

    private fun takeFirstThatIsNotAReactorOfTheRest(chemicals: MutableList<ReactionComponent>): ReactionComponent {
        val chemical = chemicals.first {
            it.isNotReactorOf(chemicals - it, reactions)
        }
        chemicals.remove(chemical)
        return chemical
    }

    private fun toReactionComponent(
        reactor: ReactionComponent,
        productRequired: Long,
        createdPerReaction: Long
    ): ReactionComponent = ReactionComponent(
        reactor.chemical,
        reactor.amountPerReaction * divideRoundedUp(productRequired, createdPerReaction)
    )

    private fun divideRoundedUp(actuallyNeeded: Long, createdPerReaction: Long) =
        ceil(actuallyNeeded / createdPerReaction.toDouble()).toLong()

}

private fun MutableList<ReactionComponent>.mergeOnChemical(): MutableList<ReactionComponent> {
    val chemicalToAmount = this.map { it.chemical to 0L }.toMap().toMutableMap()
    this.forEach { component ->
        chemicalToAmount.merge(component.chemical, component.amountPerReaction, Long::plus)
    }
    return chemicalToAmount.entries.map { ReactionComponent(it.key, it.value) }.toMutableList()
}
