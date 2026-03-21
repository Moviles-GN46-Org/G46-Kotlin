package com.example.g46_kotlin.features.map.presentation

private val whitespaceRegex = Regex("\\s+")

/**
 * Convierte COP a "miles COP":
 * 1_000_000 -> "1'000"
 * 950_000   -> "950"
 */
fun formatCopToThousandsLabel(rawPrice: String): String {
    val amountCop = rawPrice.filter { it.isDigit() }.toLongOrNull() ?: 0L
    val thousands = amountCop / 1_000L
    return thousands
        .toString()
        .reversed()
        .chunked(3)
        .joinToString("'")
        .reversed()
}

/**
 * Toma solo 2-3 primeras palabras y agrega "..." si fue recortado.
 */
fun shortenTitleForMap(title: String, maxWords: Int = 3): String {
    val words = title.trim().split(whitespaceRegex).filter { it.isNotBlank() }
    if (words.size <= maxWords) return title.trim()
    return words.take(maxWords).joinToString(" ") + "..."
}
