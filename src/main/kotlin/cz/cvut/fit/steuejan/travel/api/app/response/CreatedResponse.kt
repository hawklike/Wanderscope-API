package cz.cvut.fit.steuejan.travel.api.app.response

import kotlinx.serialization.Serializable

@Serializable
data class CreatedResponse(
    val id: Int
) : Response by Success(Status.CREATED) {
    companion object {
        fun success(id: Int) = CreatedResponse(id)
    }
}
