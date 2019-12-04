package com.github.shmvanhouten.adventofcode2019.day04

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day04Test {

    @Test
    internal fun `a password must never decrease`() {
        assertThat(isValid(111111), equalTo(true) )
        assertThat(isValid(111110), equalTo(false))
        assertThat(isValid(222221), equalTo(false))
        assertThat(isValid(222212), equalTo(false))
        assertThat(isValid(123356), equalTo(true))
    }

    @Test
    internal fun `a password must have a repeating digit`() {
        assertThat(isValid(123456), equalTo(false))
        assertThat(isValid(123345), equalTo(true))
    }

    @Test
    internal fun part1() {
        println(countValidPasswordsInRange(165432..707912))
        // 1716
    }

    @Nested
    inner class Part2 {
        @Test
        internal fun `a password must have a digit repeat exactly twice`() {
            assertThat(isValid2(112233), equalTo(true))
            assertThat(isValid2(123444), equalTo(false))
            assertThat(isValid2(111122), equalTo(true))
        }

        @Test
        internal fun part2() {
            println(countValidPasswordsInRange2(165432..707912))
            // 1163
        }
    }
}
