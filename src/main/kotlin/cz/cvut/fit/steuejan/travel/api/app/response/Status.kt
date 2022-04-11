package cz.cvut.fit.steuejan.travel.api.app.response

enum class Status {
    SUCCESS, ACCEPTED, NO_CONTENT, CREATED,
    UNAUTHORIZED, BAD_REQUEST, FORBIDDEN, INTERNAL_ERROR, NOT_FOUND;

    fun isSuccess() = this in listOf(SUCCESS, ACCEPTED, NO_CONTENT, CREATED)
}