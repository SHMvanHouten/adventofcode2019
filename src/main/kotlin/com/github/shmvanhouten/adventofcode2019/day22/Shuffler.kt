package com.github.shmvanhouten.adventofcode2019.day22

import com.github.shmvanhouten.adventofcode2017.util.splitIntoTwo

fun shuffle(deckSize: Int, instructions: List<ShuffleInstruction>): List<Int> {
    return instructions.fold(0.until(deckSize).toList()) { deck, instruction -> performInstruction(deck, instruction) }
}

fun findPositionOfCard(cardNumber: Int, deck: List<Int>): Int {
    return deck.mapIndexed { i, n -> i to n }.find { it.second == cardNumber }?.first ?: throw IllegalStateException("Card $cardNumber not found")
}

fun performInstruction(deck: List<Int>, instruction: ShuffleInstruction): List<Int> {
    return when(instruction.type) {
        Instructiontype.DEAL_WITH_INCREMENT -> dealWithIncrement(deck, instruction.number)
        Instructiontype.CUT -> cut(deck, instruction.number)
        Instructiontype.DEAL_INTO_NEW_STACK -> deck.reversed()
    }
}

fun dealWithIncrement(deck: List<Int>, amount: Int): List<Int> {
    val newDeck = IntArray(deck.size)
    deck.forEachIndexed{ i, n ->
        newDeck[(i * amount) % deck.size] = n
    }
    return newDeck.toList()
}

fun cut(deck: List<Int>, amount: Int): List<Int> {
    val index = if(amount < 0) {
        deck.size + amount
    } else {
        amount
    }
    val (first, second) = deck.splitIntoTwo(index)
    return second + first
}
