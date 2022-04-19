package cz.cvut.fit.steuejan.travel.api.auth.model

import kotlinx.serialization.Serializable

@Serializable
enum class AccountType {
    EMAIL, GOOGLE;

    companion object {
        const val MAX_LENGTH = 6
    }
}