package com.github.shmvanhouten.adventofcode2019.util

import com.github.shmvanhouten.adventofcode2019.day02.ExecutionType
import com.github.shmvanhouten.adventofcode2019.day02.IComputer
import java.util.*

class FakeComputer : IComputer {
    override val output: Queue<Long> = LinkedList<Long>()

    val inputs = LinkedList<Long>()

    var executionType = ExecutionType.REQUIRES_INPUT

    override fun input(input: Long): ExecutionType {
        inputs += input
        return executionType
    }

    override fun copy(): IComputer {
        return this
    }
}