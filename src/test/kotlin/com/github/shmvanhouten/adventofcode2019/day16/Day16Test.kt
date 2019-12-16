package com.github.shmvanhouten.adventofcode2019.day16

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day16Test {

    val input = "59765216634952147735419588186168416807782379738264316903583191841332176615408501571822799985693486107923593120590306960233536388988005024546603711148197317530759761108192873368036493650979511670847453153419517502952341650871529652340965572616173116797325184487863348469473923502602634441664981644497228824291038379070674902022830063886132391030654984448597653164862228739130676400263409084489497532639289817792086185750575438406913771907404006452592544814929272796192646846314361074786728172308710864379023028807580948201199540396460310280533771566824603456581043215999473424395046570134221182852363891114374810263887875638355730605895695123598637121"
    @Nested
    inner class Part1 {

        @Test
        internal fun `testInput`() {
            assertThat(cleanUpSignal("12345678", 1).substring(0, 8), equalTo("48226158"))
            assertThat(cleanUpSignal("12345678", 2).substring(0, 8), equalTo("34040438"))
            assertThat(cleanUpSignal("12345678", 3).substring(0, 8), equalTo("03415518"))
            assertThat(cleanUpSignal("12345678", 4).substring(0, 8), equalTo("01029498"))
            assertThat(cleanUpSignal("80871224585914546619083218645595", 100).substring(0, 8), equalTo("24176176"))
            assertThat(cleanUpSignal("19617804207202209144916044189917", 100).substring(0, 8), equalTo("73745418"))
            assertThat(cleanUpSignal("69317163492948606335995924319873", 100).substring(0, 8), equalTo("52432133"))
        }

        @Test
        internal fun part1() {
            assertThat(cleanUpSignal(input, 100).substring(0, 8), equalTo("70856418"))
        }

    }

    @Nested
    inner class Part2 {

        @Test
        fun testInput1 () {
            assertThat(quickFind("03036732577212944063491565474664".repeat(10000)), equalTo("84462026"))
        }

        @Test
        fun testInput2 () {
            assertThat(quickFind("02935109699940807407585447034323".repeat(10000)), equalTo("78725270"))
        }

        @Test
        fun testInput3 () {
            assertThat(quickFind("03081770884921959731165446850517".repeat(10000)), equalTo("53553731"))
        }

        @Test
        internal fun part2() {
            assertThat(quickFind(input.repeat(10000)), equalTo("87766336"))
        }
    }


}
