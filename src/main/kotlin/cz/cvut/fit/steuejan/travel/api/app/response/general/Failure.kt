package cz.cvut.fit.steuejan.travel.api.app.response.general

import kotlinx.serialization.Serializable

@Serializable
data class Failure(
    override val status: Status,
    override val message: String,
    override val code: Int = 0
) : Response

