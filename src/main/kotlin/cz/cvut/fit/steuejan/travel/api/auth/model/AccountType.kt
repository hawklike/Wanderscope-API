package cz.cvut.fit.steuejan.travel.api.auth.model

import kotlinx.serialization.Serializable

@Serializable
enum class AccountType {
    EMAIL, GOOGLE
}