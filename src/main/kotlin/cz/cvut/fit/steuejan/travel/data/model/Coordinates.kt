package cz.cvut.fit.steuejan.travel.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Coordinates(
    val longitude: String? = null,
    val latitude: String? = null
)
