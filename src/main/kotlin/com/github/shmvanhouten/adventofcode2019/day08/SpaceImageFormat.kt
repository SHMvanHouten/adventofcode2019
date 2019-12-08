package com.github.shmvanhouten.adventofcode2019.day08

fun calculateChecksum(image: Image): Int {
    val targetFlatLayer = image
        .map { it.flatten() }
        .minBy { it.count { pixel -> pixel == 0 } }
    checkNotNull(targetFlatLayer) { "You provided an empty image, you doofus" }

    return targetFlatLayer.count { it == 1 } * targetFlatLayer.count { it == 2 }
}

fun draw(image: Image) {
    val width = image[0][0].size
    val height = image[0].size
    repeat(width) { print('X')}
    println()
    0.until(height).forEach { y ->
        print('X')
        0.until(width).forEach { x ->
            val pixelPerLayer = image.map { it[y][x] }
            print(pixelize(pixelPerLayer.first { it != 2 }))
        }
        println()
    }
    repeat(width) { print('X')}
}

fun pixelize(pixel: Pixel): Char {
    return if(pixel == 0) {
        'X'
    } else {
        ' '
    }
}

fun decode(input: String, width: Int, height: Int): Image {
    val pixels = input.map { toInt(it) }
    val layerSize = width * height
    return 0.until(pixels.size).step(layerSize)
        .map { pixels.subList(it, it + layerSize) }
        .map { toLayer(it, width) }
}

fun toLayer(layer: List<Int>, width: Int): Layer {
    return 0.until(layer.size).step(width)
        .map { layer.subList(it, it + width) }
}

typealias Image = List<Layer>

typealias Layer = List<Row>

typealias Row = List<Pixel>

typealias Pixel = Int

private fun toInt(it: Char) = it.toInt().minus('0'.toInt())
