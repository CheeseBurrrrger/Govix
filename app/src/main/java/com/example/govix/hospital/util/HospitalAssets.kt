package com.example.govix.hospital.util

import com.example.govix.R

object HospitalAssets {

    fun logoDrawableRes(hospitalName: String?): Int {
        val key = hospitalName?.lowercase().orEmpty()
        return when {
            key.contains("soetomo") -> R.drawable.rsud_soetomo
            key.contains("saiful") -> R.drawable.rsud_saiful
            key.contains("haji") -> R.drawable.rsud_haji
            key.contains("karsa") -> R.drawable.rsud_karsa
            key.contains("daha") -> R.drawable.rsud_daha
            else -> R.drawable.logo_aplikasi
        }
    }

    fun operationalHoursFallback(hospitalName: String?): String =
        if (hospitalName?.contains("Daha", ignoreCase = true) == true) {
            "Senin - Jumat: 07.00 - 21.00 WIB"
        } else {
            "Senin - Minggu: 24 Jam"
        }

    fun websiteFallback(hospitalName: String?): String? = when {
        hospitalName?.contains("Soetomo", ignoreCase = true) == true ->
            "https://rsudrsoetomo.jatimprov.go.id/"
        hospitalName?.contains("Daha", ignoreCase = true) == true ->
            "https://rsuddahahusada.jatimprov.go.id/"
        hospitalName?.contains("Haji", ignoreCase = true) == true ->
            "https://app.rsuhaji.jatimprov.go.id/online/"
        hospitalName?.contains("Saiful", ignoreCase = true) == true ->
            "https://rsusaifulanwar.jatimprov.go.id/v2/"
        hospitalName?.contains("Karsa", ignoreCase = true) == true ->
            "https://rsukarsahusadabatu.jatimprov.go.id/"
        else -> null
    }
}
