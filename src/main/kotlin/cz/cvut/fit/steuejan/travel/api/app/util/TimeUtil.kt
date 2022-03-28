package cz.cvut.fit.steuejan.travel.api.app.util

import java.util.*

fun Date.isExpired(): Boolean {
    return this.before(Date())
}