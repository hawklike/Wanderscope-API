package cz.cvut.fit.steuejan.travel.api.app.extension

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.charset.Charset
import java.util.*

fun String.isNameAllowed(): Boolean {
    val regex = Regex("^[^\r\n;~]+$")
    return this matches regex
}

suspend fun String.fromBase64(charset: Charset = Charsets.UTF_8): String {
    return withContext(Dispatchers.IO) {
        Base64.getDecoder().decode(this@fromBase64).toString(charset)
    }
}