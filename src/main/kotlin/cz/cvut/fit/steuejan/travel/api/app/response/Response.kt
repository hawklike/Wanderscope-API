package cz.cvut.fit.steuejan.travel.api.app.response

import cz.cvut.fit.steuejan.travel.api.app.response.Status.*

interface Response {
    val status: Status
    val message: String
    val code: Int

    fun isSuccess(): Boolean = status in listOf(SUCCESS, ACCEPTED, NO_CONTENT, CREATED)
}