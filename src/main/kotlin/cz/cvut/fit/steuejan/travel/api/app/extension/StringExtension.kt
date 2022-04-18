package cz.cvut.fit.steuejan.travel.api.app.extension

fun String.isNameAllowed(): Boolean {
    val regex = Regex("^[^\r\n;~]+$")
    return this matches regex
}