package cz.cvut.fit.steuejan.travel.api.app.response

import kotlinx.serialization.Serializable

@Serializable
data class CreatedResponse(
    val id: Long
) : Response by Success(Status.CREATED) {
    companion object {
        fun success(id: Long) = CreatedResponse(id)
        fun success(id: Int) = CreatedResponse(id.toLong())
    }
}
