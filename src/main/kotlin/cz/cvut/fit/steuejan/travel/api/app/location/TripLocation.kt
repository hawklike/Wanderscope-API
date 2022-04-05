package cz.cvut.fit.steuejan.travel.api.app.location

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location(Trip.URL)
object Trip {

    @Location(Invite.URL)
    class Invite {
        companion object {
            const val URL = "/invite"
        }
    }

    const val URL = "/trip"
}