package com.github.shmvanhouten.adventofcode2019.day23

import com.github.shmvanhouten.adventofcode2019.day02.Computer
import com.github.shmvanhouten.adventofcode2019.day02.IntCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

fun network(intCode: IntCode) = runBlocking {

    val output = Channel<Packet>()
    val computers = build50Computers(intCode)

    val packetDistributor = computers.map { it.address to mutableListOf<Packet>() }.toMap()

    launchPacketDistributor(packetDistributor, output)
    launchComputers(computers, output)
    distributeOutput(packetDistributor, computers)
}

private fun build50Computers(intCode: IntCode): List<NetworkComputer> {
    return 0L.until(50L).map { address ->
        val computer = Computer(intCode)
        computer.input(address)

        val input = Channel<Packet>()
        NetworkComputer(address, input, computer)
    }
}

private fun CoroutineScope.launchComputers(
    computers: List<NetworkComputer>,
    output: Channel<Packet>
) {
    computers.forEach {
        launchNetworkComputer(it.input, output, it.computer)
    }
}

private fun CoroutineScope.launchPacketDistributor(
    packetDistributor: Map<Address, MutableList<Packet>>,
    output: Channel<Packet>
) {
    launch {
        while (true) {
            val packet = output.receive()
            packetDistributor[packet.address]?.add(packet) ?: throw Exception("Packet found: $packet")
        }
    }
}

private fun CoroutineScope.distributeOutput(
    packetDistributor: Map<Long, MutableList<Packet>>,
    computers: List<NetworkComputer>
) = launch {
    val computersByAddress = computers.map { it.address to it }.toMap()
    generateSequence(computers){l -> l + computers}.flatten().forEach { computer ->
        val packets = packetDistributor[computer.address]?: throw IllegalStateException("No packets found at address ${computer.address}")
        val packet = if(packets.isNotEmpty()) {
            packets.removeAt(0)
        } else {
            Packet(computer.address, -1, -1)
        }
        computersByAddress[computer.address]?.input?.send(packet) ?: throw IllegalStateException("No computer found at address ${computer.address}")
    }
}

fun CoroutineScope.launchNetworkComputer(
    receivedPackets: ReceiveChannel<Packet>,
    output: SendChannel<Packet>,
    computer: Computer
) = launch {
    while (isActive) {
        val packet = receivedPackets.receive()
        if (packet.x == -1L) {
            computer.input(-1L)
        } else {
            println(packet.address)
            computer.input(packet.x)
            computer.input(packet.y)
        }

        while (computer.output.isNotEmpty()) {
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
    val computer: Computer
)

data class Packet(val address: Address, val x: Long, val y: Long)

typealias Address = Long