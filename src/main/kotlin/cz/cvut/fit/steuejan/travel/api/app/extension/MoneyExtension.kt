package cz.cvut.fit.steuejan.travel.api.app.extension

import kotlin.math.round
import kotlin.math.roundToLong

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}

fun Long.fromCents() = this / 100.0

fun Double.toCents(): Long = (this.round(2) * 100).roundToLong()