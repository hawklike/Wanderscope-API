@file:OptIn(KtorExperimentalLocationsAPI::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package cz.cvut.fit.steuejan.travel.api.app.location

import cz.cvut.fit.steuejan.travel.api.trip.model.GetTripsType
import io.ktor.locations.*
import org.joda.time.DateTime

@Location(User.URL)
object User {

    @Location(Trip.URL)
    class Trip {
        companion object {
            const val URL = "/trip"
        }
    }

    @Location(Trips.URL)
    class Trips(val scope: GetTripsType = GetTripsType.ALL, val date: DateTime? = null) {
        companion object {
            const val URL = "/trips"
        }
    }

    const val URL = "/user"
}