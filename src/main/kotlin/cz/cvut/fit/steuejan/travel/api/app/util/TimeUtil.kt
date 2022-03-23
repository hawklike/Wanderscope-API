package cz.cvut.fit.steuejan.travel.api.app.util

import java.util.*

fun isExpired(expiresAt: Date): Boolean {
    return expiresAt.before(Date())
}