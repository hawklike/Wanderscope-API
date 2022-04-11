package cz.cvut.fit.steuejan.travel.api.app.conversion

import cz.cvut.fit.steuejan.travel.api.trip.model.GetTripsType
import io.ktor.features.*
import io.ktor.util.*

fun DataConversion.Configuration.convertGetTripsType() {
    convert<GetTripsType> {
        encode { value ->
            when (value) {
                null -> emptyList()
                is GetTripsType -> listOf(value.name.lowercase())
                else -> throw DataConversionException("cannot convert $value as GetTripsType")
            }
        }
        decode { values, _ ->
            val lowercaseValues = values.map { it.lowercase() }
            GetTripsType.values().first {
                it.name.lowercase() in lowercaseValues
            }
        }
    }
}