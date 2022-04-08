package cz.cvut.fit.steuejan.travel.api.app.statuspages

import cz.cvut.fit.steuejan.travel.api.app.exception.*
import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.NotFoundException
import cz.cvut.fit.steuejan.travel.api.app.extension.respond
import cz.cvut.fit.steuejan.travel.api.app.response.Failure
import cz.cvut.fit.steuejan.travel.api.app.response.Status
import io.ktor.features.*

fun StatusPages.Configuration.generalStatusPages() {
    exception<BadRequestException> {
        respond(Failure(Status.BAD_REQUEST, it.message))
    }

    exception<UnauthorizedException> {
        respond(Failure(Status.UNAUTHORIZED, it.message))
    }

    exception<NotFoundException> {
        respond(Failure(Status.NOT_FOUND, it.message))
    }

    exception<InternalServerErrorException> {
        respond(Failure(Status.INTERNAL_ERROR, it.message))
    }

    exception<ForbiddenException> {
        respond(Failure(Status.FORBIDDEN, it.message))
    }
}