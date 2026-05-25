package com.example.govix.core.util

import java.util.Locale

fun parseDdMmYyyyToIsoOrNull(input: String): String? {
    val parts = input.trim().split("/")
    if (parts.size != 3) return null
    val day = parts[0].toIntOrNull() ?: return null
    val month = parts[1].toIntOrNull() ?: return null
    val year = parts[2].toIntOrNull() ?: return null
    if (day !in 1..31 || month !in 1..12 || year !in 1900..2100) return null
    return String.format(Locale.US, "%04d-%02d-%02d", year, month, day)
}
fun genderLabelToApi(label: String): String = when (label.trim()) {
    "Laki - Laki", "Laki-Laki", "Laki Laki" -> "L"
    "Perempuan" -> "P"
    else -> when (label.uppercase(Locale.US)) {
        "L" -> "L"
        "P" -> "P"
        else -> label
    }
}