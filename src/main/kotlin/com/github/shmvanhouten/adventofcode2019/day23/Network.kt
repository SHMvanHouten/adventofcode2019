package com.github.shmvanhouten.adventofcode2019.day23

import com.github.shmvanhouten.adventofcode2019.day02.Computer
import com.github.shmvanhouten.adventofcode2019.day02.IntCode
import com.github.shmvanhouten.adventofcode2019.day23.NetworkComputerState.ACTIVE
import com.github.shmvanhouten.adventofcode2019.day23.NetworkComputerState.IDLE
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import java.util.*

fun network(intCode: IntCode) = runBlocking {

    val output = Channel<Packet>()
    val computers = build50Computers(intCode, output)

    val packetDistributor = computers.map { it.address to mutableListOf<Packet>() }.plus(255L to mutableListOf()).toMap()

    launchPacketDistributor(packetDistributor, output)
    launchComputers(computers)
    distributeOutput(packetDistributor, computers)
}

private fun build50Computers(
    intCode: IntCode,
    output: Channel<Packet>
): List<NetworkComputer> {
    return 0L.until(50L).map { address ->
        val computer = Computer(intCode)
        computer.input(address)

        val input = Channel<Packet>()
        NetworkComputer(address, input, output, computer)
    }
}

private fun CoroutineScope.launchComputers(
    computers: List<NetworkComputer>
) {
    computers.forEach {
        launchNetworkComputer(it)
    }
}

private fun CoroutineScope.launchPacketDistributor(
    packetDistributor: Map<Address, MutableList<Packet>>,
    output: Channel<Packet>
) {
    launch {
        while (true) {
            val packet = output.receive()
            packetDistributor[packet.address]?.add(packet)
        }
    }
}

private fun CoroutineScope.distributeOutput(
    packetDistributor: Map<Long, MutableList<Packet>>,
    computers: List<NetworkComputer>
) = launch {
    val computersByAddress = computers.map { it.address to it }.toMap()
    while (isActive) {
        if(computers.all { it.state == IDLE } && theyAreStillIdle(computers)) {
            val packet = packetDistributor[255L]?.last()?: throw IllegalStateException("No packets found at address 255")
            println(packet)
            computersByAddress[0]?.input?.send(packet)
            computers.asSequence().forEach { it.state = ACTIVE }
        } else {
            computers.asSequence().forEach { computer ->
                val packets = packetDistributor[computer.address]
                    ?: throw IllegalStateException("No packets found at address ${computer.address}")
                val packet = if (packets.isNotEmpty()) {
                    packets.removeAt(0)
                } else {
                    Packet(computer.address, -1, -1)
                }
                computersByAddress[computer.address]?.input?.send(packet)
                    ?: throw IllegalStateException("No computer found at address ${computer.address}")
            }
        }
    }
}

suspend fun theyAreStillIdle(computers: List<NetworkComputer>): Boolean {
    delay(100)
    return computers.all { it.state == IDLE }
}

fun CoroutineScope.launchNetworkComputer(
    networkComputer: NetworkComputer
) = launch {
    val computer = networkComputer.computer
    val receivedPackets = networkComputer.input
    val output = networkComputer.output
    while (isActive) {
        val packet = receivedPackets.receive()
        if (packet.x == -1L) {
            if(computer.output.isEmpty()) {
                networkComputer.state = IDLE
            }
            computer.input(-1L)
        } else {
            networkComputer.state = ACTIVE
            println(packet.address)
            computer.input(packet.x)
            computer.input(packet.y)
        }

        while (computer.output.isNotEmpty()) {
            networkComputer.state = ACTIVE
            output.send(toPacket(computer.output))
        }
    }
}

private fun toPacket(output: Queue<Long>): Packet {
    val address = output.poll()
    val x = output.poll()
    val y = output.poll()
    return Packet(address, x, y)
}

data class NetworkComputer(
    val address: Long,
    val input: Channel<Packet>,
    val output: SendChannel<Packet>,
    val computer: Computer,
    var state: NetworkComputerState = ACTIVE
)

data class Packet(val address: Address, val x: Long, val y: Long)

typealias Address = Long

private fun Map<Address, MutableList<Packet>>.getPacket(address: Address): MutableList<Packet> {
    return this[address] ?: throw IllegalStateException("No packets found at address $address")
}

enum class NetworkComputerState {
    ACTIVE,
    IDLE
}