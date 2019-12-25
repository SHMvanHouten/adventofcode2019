package com.github.shmvanhouten.adventofcode2019.day22

fun shuffleToList(deckSize: Long, instructions: List<ShuffleInstruction>): List<Long> {
    return shuffle(deckSize, instructions).toSortedMap().values.toList()
}

fun shuffle(deckSize: Long, instructions: List<ShuffleInstruction>): Map<Long, Long> {
    return instructions.fold(0L.until(deckSize).map { it to it }.toMap()) { deck, instruction -> performInstruction(deck, instruction, deckSize) }
}

fun findPositionOfCard(cardNumber: Long, deck: Map<Long, Long>): Long {
    return deck.entries.find { it.value == cardNumber }?.key ?: throw IllegalStateException("Card $cardNumber not found")
}

fun performInstruction(
    deck: Map<Long, Long>,
    instruction: ShuffleInstruction,
    deckSize: Long
): Map<Long, Long> {
    return when(instruction.type) {
        Instructiontype.DEAL_WITH_INCREMENT -> dealWithIncrement(deck, instruction.number, deckSize)
        Instructiontype.CUT -> cut(deck, instruction.number, deckSize)
        Instructiontype.DEAL_INTO_NEW_STACK -> reverse(deck)
    }
}

fun reverse(deck: Map<Long, Long>): Map<Long, Long> {
    val sortedDeck = deck.toSortedMap()
    return sortedDeck.keys.zip(sortedDeck.values.reversed()).toMap()
}

fun dealWithIncrement(
    deck: Map<Long, Long>,
    amount: Long,
    deckSize: Long
): Map<Long, Long> {
    val newDeck = mutableMapOf<Long, Long>()
    0L.until(deckSize).map { i ->
        newDeck[(i * amount) % deckSize] = deck[i]!!
    }
    return newDeck
}

fun cut(deck: Map<Long, Long>, amount: Long, deckSize: Long): Map<Long, Long> {
    val index = if(amount < 0) {
        deckSize + amount
    } else {
        amount
    }
    return deck.moveDeckFromIndexToStart(index, deckSize)
}

private fun Map<Long, Long>.moveDeckFromIndexToStart(index: Long, deckSize: Long): Map<Long, Long> {
    return 0L.until(deckSize).map { it to this[(index + it) % deckSize]!! }.toMap()
}
