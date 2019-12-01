package com.github.shmvanhouten.adventofcode2019.day01

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class CalculateFuelTest {

    @Nested
    inner class Part1 {

        // Calculate the fuel consumption of a module with the equation fuel = mass / 3 - 2

        @Test
        internal fun `a mass of 12 requires 2 fuel`() {
            assertThat(calculateFuelSum(12), equalTo(2))
        }

        @Test
        internal fun `a mass of 14 requires 2 fuel`() {
            assertThat(calculateFuelSum(14), equalTo(2))
        }

        @Test
        internal fun `a mass of 15 requires 3 fuel`() {
            assertThat(calculateFuelSum(15), equalTo(3))
        }

        @Test
        internal fun `a mass of 22 requires 5`() {
            assertThat(calculateFuelSum(22), equalTo(5))
        }

        @Test
        internal fun `a mass of 1969 requires 654`() {
            assertThat(calculateFuelSum(1969), equalTo(654))
        }

        @Test
        internal fun `a mass of 100756 requires 33583`() {
            assertThat(calculateFuelSum(100756), equalTo(33583))
        }

        @Test
        internal fun `to calculate the fuel requirement of multiple modules, add their fuel requirements together`() {
            assertThat(calculateFuelSum(12), equalTo(2))
            assertThat(calculateFuelSum(14), equalTo(2))
            assertThat(calculateFuelSum(1969, 100756), equalTo(654 + 33583))
        }

        @Test
        internal fun `part 1`() {
            println(calculateFuelSum(input))
        }
    }

    @Nested
    inner class Part2 {
        @Test
        internal fun `a mass of 14 requires 2 fuel`() {
            assertThat(calculateCumulativeFuelSum(14), equalTo(2))
        }

        @Test
        internal fun `a mass of 15 requires 3 fuel`() {
            assertThat(calculateCumulativeFuelSum(15), equalTo(3))
        }

        @Test
        internal fun `a mass of 33 requires 9 + 1 fuel`() {
            assertThat(calculateCumulativeFuelSum(33), equalTo(10))
        }

        @Test
        internal fun `a mass of (33 + 2) * 3 = 105 requires 33 + 9 + 1 fuel`() {
            assertThat(calculateCumulativeFuelSum(105), equalTo(33 + 9 + 1))
        }

        @Test
        internal fun `a mass of 1969 requires 966 fuel`() {
            assertThat(calculateCumulativeFuelSum(1969), equalTo(966))
        }

        @Test
        internal fun `a mass of 100756 requires 50346 fuel`() {
            assertThat(calculateCumulativeFuelSum(100756), equalTo(50346))
        }

        @Test
        internal fun `part 2`() {
            println(calculateCumulativeFuelSum(input))
        }
    }

}

val input = """83568
132382
65095
105082
138042
59055
79113
123950
59773
55031
56499
122835
123608
82848
109981
115633
126241
137240
54983
129523
101517
90879
82446
105897
108653
130530
113607
140338
125646
112605
68080
105466
93462
147116
127370
128362
83129
146946
102658
62824
52950
119301
61671
92820
139579
93816
148535
77893
80523
69543
51773
144074
100340
64565
68404
88923
144824
87836
51209
99770
111044
144978
56585
137236
73290
86608
72415
57783
130619
109599
59655
99708
118488
104989
93812
135899
110396
89346
119482
67292
143810
64085
104169
145618
104035
75765
88638
139325
89099
132807
117255
98029
114780
104708
100671
98052
141263
149844
117643
123410"""