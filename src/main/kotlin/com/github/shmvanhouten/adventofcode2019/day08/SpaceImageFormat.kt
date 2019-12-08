package com.github.shmvanhouten.adventofcode2019.day08

fun calculateChecksum(image: Image): Int {
    val targetFlatLayer = image
        .map { it.flatten() }
        .minBy { it.count { pixel -> pixel == 0 } }
    checkNotNull(targetFlatLayer) { "You provided an empty image, you doofus" }

    return targetFlatLayer.count { it == 1 } * targetFlatLayer.count { it == 2 }
}

fun decode(input: String, width: Int, height: Int): Image {
    val pixels = input.map { toInt(it) }
    val layerSize = width * height
    return 0.until(pixels.size).step(layerSize)
        .map { pixels.subList(it, it + layerSize) }
        .map { toLayer(it, width) }
}

private fun toLayer(layer: List<Int>, width: Int): Layer {
    return 0.until(layer.size).step(width)
        .map { layer.subList(it, it + width) }
}

typealias Image = List<Layer>

typealias Layer = List<Row>

typealias Row = List<Pixel>

typealias Pixel = Int

private fun toInt(it: Char) = it.toInt().minus('0'.toInt())
