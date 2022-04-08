package cz.cvut.fit.steuejan.travel.api.app.conversion

import io.ktor.features.*
import io.ktor.util.*
import org.joda.time.DateTime

fun DataConversion.Configuration.convertDateTime() {
    convert<DateTime> {
        encode { value ->
            when (value) {
                null -> emptyList()
                is DateTime -> listOf(value.toString())
                else -> throw DataConversionException("cannot convert $value as DateTime")
            }
        }
        decode { values, _ ->
            values.single().let { DateTime.parse(it) }
        }
    }
}