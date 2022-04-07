package cz.cvut.fit.steuejan.travel.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val googlePlaceId: String? = null,
    val address: String? = null
)
