package cz.cvut.fit.steuejan.travel.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Contact(
    val phone: String? = null,
    val email: String? = null,
    val website: String? = null
)
