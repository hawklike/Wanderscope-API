package cz.cvut.fit.steuejan.travel.api.app.extension

import cz.cvut.fit.steuejan.travel.api.app.exception.UnauthorizedException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.app.request.Request
import cz.cvut.fit.steuejan.travel.api.app.response.general.Response
import cz.cvut.fit.steuejan.travel.api.app.response.general.generateHttpResponse
import cz.cvut.fit.steuejan.travel.api.app.util.parseBodyOrBadRequest
import cz.cvut.fit.steuejan.travel.api.auth.jwt.UsernamePrincipal
import cz.cvut.fit.steuejan.travel.data.model.Username
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.util.pipeline.*

suspend inline fun <reified T : Request> PipelineContext<*, ApplicationCall>.receive(message: String): T {
    return parseBodyOrBadRequest(message) {
        call.receive()
    }
}

suspend fun PipelineContext<*, ApplicationCall>.respond(response: Response) {
    with(generateHttpResponse(response)) {
        call.respond(code, body)
    }
}

fun PipelineContext<*, ApplicationCall>.getUsername(): Username {
    return call.principal<UsernamePrincipal>()?.username
        ?: throw UnauthorizedException(FailureMessages.JWT_SUB_MISSING)
}