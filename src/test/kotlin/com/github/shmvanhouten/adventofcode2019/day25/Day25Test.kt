package com.github.shmvanhouten.adventofcode2019.day25

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day25Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `part 1`() {
            assertThat(findThePassword(), equalTo("1090529280") )
        }
    }

}
