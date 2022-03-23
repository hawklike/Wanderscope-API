package cz.cvut.fit.steuejan.travel.api.app.conversion

import cz.cvut.fit.steuejan.travel.api.auth.model.AccountType
import io.ktor.features.*
import io.ktor.util.*

fun DataConversion.Configuration.convertAuthFlow() {
    convert<AccountType> {
        encode { value ->
            when (value) {
                null -> emptyList()
                is AccountType -> listOf(value.name.lowercase())
                else -> throw DataConversionException("cannot convert $value as AccountType")
            }
        }
        decode { values, _ ->
            val lowercaseValues = values.map { it.lowercase() }
            AccountType.values().first {
                it.name.lowercase() in lowercaseValues
            }
        }
    }
}