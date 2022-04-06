package cz.cvut.fit.steuejan.travel.api.app.location

import cz.cvut.fit.steuejan.travel.api.trip.model.GetTripsType
import io.ktor.locations.*
import org.joda.time.DateTime

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

@KtorExperimentalLocationsAPI
@Location(Trips.URL)
class Trips(val scope: GetTripsType = GetTripsType.ALL, val date: DateTime? = null) {
    companion object {
        const val URL = "/trips"
    }
}