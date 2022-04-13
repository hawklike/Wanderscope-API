package cz.cvut.fit.steuejan.travel.api.app.statuspages

import cz.cvut.fit.steuejan.travel.api.app.extension.respond
import cz.cvut.fit.steuejan.travel.api.app.response.Failure
import cz.cvut.fit.steuejan.travel.api.app.response.Status
import cz.cvut.fit.steuejan.travel.api.trip.exception.ChangeRoleException
import io.ktor.features.*

fun StatusPages.Configuration.changeRoleStatusPages() {
    exception<ChangeRoleException> {
        respond(Failure(Status.FORBIDDEN, it.message, it.code))
    }
}