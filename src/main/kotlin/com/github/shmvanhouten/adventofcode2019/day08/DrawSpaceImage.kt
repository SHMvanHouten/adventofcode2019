package com.github.shmvanhouten.adventofcode2019.day08

fun draw(image: Image) =
            drawBlackLine(image.width()) +
            drawImage(image) +
            drawBlackLine(image.width())

private fun drawImage(image: Image) =
    0.until(image.height()).joinToString("") { y ->
        'X' + drawRow(image.width(), getRowPerLayer(image, y)) + '\n'
    }

private fun drawRow(width: Int, row: List<Row>) =
    0.until(width).map { x ->
        draw(firstOpaquePixel(getPixelPerLayer(row, x)))
    }.joinToString("")

private fun drawBlackLine(width: Int) =
    (0..width).map { 'X' }.joinToString("") + '\n'

private fun firstOpaquePixel(pixels: List<Pixel>) =
    pixels.first { it != 2 }

private fun getRowPerLayer(image: Image, y: Int) =
    image.map { it[y] }

private fun getPixelPerLayer(rowsPerLayer: List<Row>, x: Int) =
    rowsPerLayer.map { it[x] }

private fun draw(pixel: Pixel): Char {
    return if (pixel == 0) {
        'X'
    } else {
        ' '
    }
}

private fun Image.width(): Int {
    return this[0][0].size
}

private fun Image.height(): Int {
    return this[0].size
}
