@file:Suppress("unused")

package cz.cvut.fit.steuejan.travel.api.app.extension

import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.UnauthorizedException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.app.request.Request
import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.app.response.generateHttpResponse
import cz.cvut.fit.steuejan.travel.api.app.util.parseBodyOrBadRequest
import cz.cvut.fit.steuejan.travel.api.trip.document.model.FileWrapper
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.util.pipeline.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

fun PipelineContext<*, ApplicationCall>.getUserId(): Int {
    try {
        return requireNotNull(call.principal<JWTPrincipal>()?.subject?.toInt())
    } catch (ex: Exception) {
        throw UnauthorizedException(FailureMessages.JWT_SUB_MISSING)
    }
}

fun PipelineContext<*, ApplicationCall>.getQuery(queryParam: String): String {
    return call.request.queryParameters[queryParam]
        ?: throw BadRequestException(FailureMessages.missingQueryParam(queryParam))
}

suspend fun PipelineContext<*, ApplicationCall>.getFile(): FileWrapper {
    var file: FileWrapper? = null
    call.receiveMultipart().forEachPart { part ->
        if (part is PartData.FileItem) {
            withContext(Dispatchers.IO) {
                val bytes = part.streamProvider().readBytes()
                val name = part.originalFileName
                    ?: throw BadRequestException(FailureMessages.MULTIPART_FORM_MISSING_FILE_NAME)
                file = FileWrapper(name, bytes)
            }
        }
    }
    return file ?: throw BadRequestException(FailureMessages.MULTIPART_FORM_MISSING_FILE)
}