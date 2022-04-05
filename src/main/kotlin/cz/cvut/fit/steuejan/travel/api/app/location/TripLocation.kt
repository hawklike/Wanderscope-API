package cz.cvut.fit.steuejan.travel.api.app.location

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location(Trip.URL)
object Trip {

    const val URL = "/trip"
}