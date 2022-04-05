package cz.cvut.fit.steuejan.travel.api.app.response

import kotlinx.serialization.Serializable

@Serializable
open class Success(
    override val status: Status = Status.SUCCESS,
    override val message: String = "",
    override val code: Int = 1
) : Response