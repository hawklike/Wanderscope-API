package cz.cvut.fit.steuejan.travel.api.app.response

import io.ktor.http.*

sealed class HttpResponse<T> {
    abstract val body: T
    abstract val code: HttpStatusCode
}

data class Ok<T : Response>(override val body: T) : HttpResponse<T>() {
    override val code: HttpStatusCode = HttpStatusCode.OK
}

data class Created<T : Response>(override val body: T) : HttpResponse<T>() {
    override val code: HttpStatusCode = HttpStatusCode.Created
}

data class Accepted<T : Response>(override val body: T) : HttpResponse<T>() {
    override val code: HttpStatusCode = HttpStatusCode.Accepted
}

data class NoContent<T : Response>(override val body: T) : HttpResponse<T>() {
    override val code: HttpStatusCode = HttpStatusCode.NoContent
}

data class BadRequest<T : Response>(override val body: T) : HttpResponse<T>() {
    override val code: HttpStatusCode = HttpStatusCode.BadRequest
}

data class Unauthorized<T : Response>(override val body: T) : HttpResponse<T>() {
    override val code: HttpStatusCode = HttpStatusCode.Unauthorized
}

data class Forbidden<T : Response>(override val body: T) : HttpResponse<T>() {
    override val code: HttpStatusCode = HttpStatusCode.Forbidden
}

data class NotFound<T : Response>(override val body: T) : HttpResponse<T>() {
    override val code: HttpStatusCode = HttpStatusCode.NotFound
}

data class InternalServerError<T : Response>(override val body: T) : HttpResponse<T>() {
    override val code: HttpStatusCode = HttpStatusCode.InternalServerError
}

fun <T : Response> generateHttpResponse(response: T): HttpResponse<T> {
    return when (response.status) {
        Status.SUCCESS -> Ok(response)
        Status.CREATED -> Created(response)
        Status.ACCEPTED -> Accepted(response)
        Status.NO_CONTENT -> NoContent(response)
        Status.BAD_REQUEST -> BadRequest(response)
        Status.UNAUTHORIZED -> Unauthorized(response)
        Status.FORBIDDEN -> Forbidden(response)
        Status.NOT_FOUND -> NotFound(response)
        Status.INTERNAL_ERROR -> InternalServerError(response)
    }
}
