package cz.cvut.fit.steuejan.travel.api.app.conversion

import cz.cvut.fit.steuejan.travel.data.model.UserRole
import io.ktor.features.*
import io.ktor.util.*

fun DataConversion.Configuration.convertUserRole() {
    convert<UserRole> {
        encode { value ->
            when (value) {
                null -> emptyList()
                is UserRole -> listOf(value.name.lowercase())
                else -> throw DataConversionException("cannot convert $value as UserRole")
            }
        }
        decode { values, _ ->
            val lowercaseValues = values.map { it.lowercase() }
            UserRole.values().first {
                it.name.lowercase() in lowercaseValues
            }
        }
    }
}