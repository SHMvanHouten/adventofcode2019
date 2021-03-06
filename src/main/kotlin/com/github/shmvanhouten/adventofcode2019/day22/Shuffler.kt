package com.github.shmvanhouten.adventofcode2019.day22

import java.math.BigInteger

fun shuffleToList(deckSize: Long, instructions: List<ShuffleInstruction>): List<Long> {
    return shuffle(deckSize, instructions).toSortedMap().values.toList()
}

fun shuffle(deckSize: Long, instructions: List<ShuffleInstruction>): Map<Long, Long> {
    return instructions
        .map { performModulusOnSize(it, deckSize) }
        .fold(
        0L.until(deckSize).map { it to it }.toMap()
    ) { deck, instruction ->
        performInstruction(deck, instruction, deckSize)
    }
}

fun performModulusOnSize(instruction: ShuffleInstruction, deckSize: Long): ShuffleInstruction {
    return ShuffleInstruction(instruction.type, instruction.number % BigInteger.valueOf(deckSize))
}

fun findPositionOfCard(cardNumber: Long, deck: Map<Long, Long>): Long {
    return deck.entries.find { it.value == cardNumber }?.key
        ?: throw IllegalStateException("Card $cardNumber not found")
}

fun performInstruction(
    deck: Map<Long, Long>,
    instruction: ShuffleInstruction,
    deckSize: Long
): Map<Long, Long> {
    return when (instruction.type) {
        Instructiontype.DEAL_WITH_INCREMENT -> dealWithIncrement(deck, instruction.number.toLong(), deckSize)
        Instructiontype.CUT -> cut(deck, instruction.number.toLong(), deckSize)
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
    val newAmount = handleNegativeAmount(amount, deckSize)
    val newDeck = mutableMapOf<Long, Long>()
    0L.until(deckSize).map { i ->
        newDeck[(i * newAmount) % deckSize] = deck[i]!!
    }
    return newDeck
}

private fun handleNegativeAmount(amount: Long, deckSize: Long): Long {
    return if (amount < 0) {
        deckSize - amount
    } else {
        amount
    }
}

fun cut(deck: Map<Long, Long>, amount: Long, deckSize: Long): Map<Long, Long> {
    val index = if (amount < 0) {
        deckSize + amount
    } else {
        amount
    }
    return deck.moveDeckFromIndexToStart(index, deckSize)
}

private fun Map<Long, Long>.moveDeckFromIndexToStart(index: Long, deckSize: Long): Map<Long, Long> {
    return 0L.until(deckSize).map { it to this[(index + it) % deckSize]!! }.toMap()
}

// Part 2
fun shuffleJustOneCardXTimes(originalIndex: Long, deckSize: Long, repeats: Long, instructions: List<ShuffleInstruction>): Long {
    var index = originalIndex
    0L.until(repeats).forEach { _ ->
        index = shuffleJustOneCard1(index, deckSize, instructions.map { performModulusOnSize(it, deckSize) })
    }
    return index
}

private fun shuffleJustOneCard1(originalIndex: Long, deckSize: Long, instructions: List<ShuffleInstruction>): Long {
    return instructions
        .fold(
        originalIndex
    ) { index, instruction ->
        performInstruction(index, instruction, deckSize)
    }
}

fun shuffleJustOneCard(originalIndex: Long, deckSize: Long, instructions: List<ShuffleInstruction>): Long {
    return instructions
        .map { performModulusOnSize(it, deckSize) }
        .fold(
        originalIndex
    ) { index, instruction ->
        performInstruction(index, instruction, deckSize)
    }
}

fun performInstruction(
    index: Long,
    instruction: ShuffleInstruction,
    deckSize: Long
): Long {
    return when (instruction.type) {
        Instructiontype.DEAL_WITH_INCREMENT -> dealWithIncrement(index, instruction.number.toLong(), deckSize)
        Instructiontype.CUT -> cut(index, instruction.number.toLong(), deckSize)
        Instructiontype.DEAL_INTO_NEW_STACK -> reverse(index, deckSize)
    }
}

fun dealWithIncrement(
    oldIndex: Long,
    amount: Long,
    deckSize: Long
): Long {
    val newAmount = handleNegativeAmount(amount, deckSize)
    return oldIndex * newAmount % deckSize
}

fun cut(oldIndex: Long, amount: Long, deckSize: Long): Long {
    return (oldIndex - amount + deckSize) % deckSize
}

fun reverse(oldIndex: Long, deckSize: Long): Long {
    return deckSize - 1 - oldIndex
}

fun cutInverse(newIndex: Long, amount: Long, deckSize: Long): Long {
    return (newIndex + amount + deckSize) % deckSize
}

fun dealWithIncrementInverse(
    newIndex: Long,
    amount: Long,
    deckSize: Long
): Long {
    val newAmount = handleNegativeAmount(amount, deckSize)
    var r = newIndex
    while (r % newAmount != 0L) {
        r += deckSize
    }
    return r/amount
}


fun inverseShuffleOneCard(index: Long, deckSize: Long, instructions: List<ShuffleInstruction>): Long {
    return instructions.reversed().fold(
        index
    ) { i, instruction ->
        performInverseInstruction(i, instruction, deckSize)
    }
}

fun performInverseInstruction(
    index: Long,
    instruction: ShuffleInstruction,
    deckSize: Long
): Long {
    return when (instruction.type) {
        Instructiontype.DEAL_WITH_INCREMENT -> dealWithIncrementInverse(index, (instruction.number % BigInteger.valueOf(deckSize)).toLong(), deckSize)
        Instructiontype.CUT -> cutInverse(index, (instruction.number % BigInteger.valueOf(deckSize)).toLong(), deckSize)
        Instructiontype.DEAL_INTO_NEW_STACK -> reverse(index, deckSize)
    }
}

fun inverseShuffleJustOneCardXTimes(originalIndex: Long, deckSize: Long, repeats: Long, instructions: List<ShuffleInstruction>): Long {
    var index = originalIndex
    0L.until(repeats).forEach { _ ->
        index = inverseShuffleOneCard(index, deckSize, instructions)
    }
    return index
}