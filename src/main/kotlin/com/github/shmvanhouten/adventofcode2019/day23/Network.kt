package com.github.shmvanhouten.adventofcode2019.day23

import com.github.shmvanhouten.adventofcode2019.day02.Computer
import com.github.shmvanhouten.adventofcode2019.day02.IntCode
import com.github.shmvanhouten.adventofcode2019.day23.NetworkComputerState.ACTIVE
import com.github.shmvanhouten.adventofcode2019.day23.NetworkComputerState.IDLE
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import java.util.*

suspend fun network(intCode: IntCode): List<Long> {

    var natPackets = emptyList<Long>()

    coroutineScope {
        val output = Channel<Packet>()
        val computers = build50Computers(intCode, output)

        val (packetDistributor, packetDistributorJob) = launchPacketDistributor(computers, output)
        val computerJobs = launchComputers(computers)

        natPackets = distributeOutput(packetDistributor, computers)
        computerJobs.forEach { it.cancelAndJoin() }
        packetDistributorJob.cancelAndJoin()
    }

    return natPackets
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
): List<Job> {
    return computers.map {
        launchNetworkComputer(it)
    }
}

private fun CoroutineScope.launchPacketDistributor(
    computers: List<NetworkComputer>,
    output: Channel<Packet>
): Pair<Map<Long, MutableList<Packet>>, Job> {
    val packetDistributor = computers.map { it.address to mutableListOf<Packet>() }.plus(255L to mutableListOf()).toMap()
    return packetDistributor to launch {
        while (true) {
            val packet = output.receive()
            packetDistributor[packet.address]?.add(packet)
        }
    }
}

private suspend fun distributeOutput(
    packetDistributor: Map<Long, MutableList<Packet>>,
    computers: List<NetworkComputer>
): MutableList<Long> {
    val computersByAddress = computers.map { it.address to it }.toMap()
    val foundYs = mutableListOf<Long>()
    while (foundYs.toSet().size == foundYs.size) {
        if (isIdle(computers, packetDistributor)) {
            val packet = getNatPacket(packetDistributor)
            foundYs.add(packet.y)
            computersByAddress[0]?.input?.send(packet)
            computers.asSequence().forEach { it.state = ACTIVE }
        } else {
            computers.asSequence().forEach { computer ->
                sendPackets(packetDistributor, computer, computersByAddress)
            }
        }
    }
    return foundYs
}

private fun getNatPacket(packetDistributor: Map<Long, MutableList<Packet>>): Packet {
    val natPackets = packetDistributor.getPacket(255)
    val packet = natPackets.last()
    println(packet)
    natPackets.clear()
    return packet
}

private suspend fun sendPackets(
    packetDistributor: Map<Long, MutableList<Packet>>,
    computer: NetworkComputer,
    computersByAddress: Map<Long, NetworkComputer>
) {
    val packets = packetDistributor.getPacket(computer.address)
    val packet = if (packets.isNotEmpty()) {
        packets.removeAt(0)
    } else {
        Packet(computer.address, -1, -1)
    }
    computersByAddress.getComputer(computer.address).input.send(packet)
}

fun CoroutineScope.launchNetworkComputer(networkComputer: NetworkComputer) = launch {
    val computer = networkComputer.computer
    val receivedPackets = networkComputer.input
    val output = networkComputer.output
    while (isActive) {
        val packet = receivedPackets.receive()
        if (packet.x == -1L) {
            if (computer.output.isEmpty()) {
                networkComputer.state = IDLE
            }
            computer.input(-1L)
        } else {
            networkComputer.state = ACTIVE
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

private fun isIdle(
    computers: List<NetworkComputer>,
    packetDistributor: Map<Long, MutableList<Packet>>
) = computers.all { it.state == IDLE } && packetDistributor.getPacket(255).isNotEmpty()


private fun Map<Address, MutableList<Packet>>.getPacket(address: Address): MutableList<Packet> {
    return this[address] ?: throw IllegalStateException("No packets found at address $address")
}
private fun Map<Address, NetworkComputer>.getComputer(address: Address): NetworkComputer {
    return this[address] ?: throw IllegalStateException("No computer found at address $address")
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

enum class NetworkComputerState {
    ACTIVE,
    IDLE
}