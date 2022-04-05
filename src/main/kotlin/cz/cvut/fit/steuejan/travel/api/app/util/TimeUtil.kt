package cz.cvut.fit.steuejan.travel.api.app.util

import org.joda.time.DateTime

fun DateTime.isExpired(): Boolean {
    return this.isBeforeNow
}