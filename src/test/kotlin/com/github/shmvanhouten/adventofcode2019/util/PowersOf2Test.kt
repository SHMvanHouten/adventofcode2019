package com.github.shmvanhouten.adventofcode2019.util

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import java.math.BigInteger

internal class PowersOf2Test {
    @Test
    internal fun `power of 2s`() {
        val target = BigInteger.valueOf(101741582076661)
        val twosToPowers = powerOf2sUntilN(target)
        val neededPowers = findNeededPowersOf2ToGetTarget(target)

        assertThat(
            neededPowers.map { power -> twosToPowers.find { it.first == power }!!.second }.reduce(BigInteger::plus),
            equalTo(target)
        )
    }
}