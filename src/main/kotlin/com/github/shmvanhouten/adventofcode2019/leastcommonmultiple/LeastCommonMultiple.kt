package com.github.shmvanhouten.adventofcode2019.leastcommonmultiple

import com.github.shmvanhouten.adventofcode2019.primefactor.primeFactors

fun leastCommonMultiple(ns: List<Long>): Long {
    return removeDuplicates(ns.map { primeFactors(it) })
        .reduce{acc: Long, l: Long -> acc * l}
}

fun removeDuplicates(primeFactorsPerNumber: List<List<Long>>): List<Long> {
    var lists = primeFactorsPerNumber.map { it.toMutableList() }
    val factors = mutableListOf<Long>()
    while (lists.isNotEmpty()) {
        val factor = lists.map { it.first() }.min()
        if(factor != null) {
            factors += factor
            lists.filter{it.contains(factor)}.forEach { it -= factor }
        }
        lists = lists.filter { it.isNotEmpty() }
    }
    return factors
}


