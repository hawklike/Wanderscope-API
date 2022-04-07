@file:OptIn(KtorExperimentalLocationsAPI::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package cz.cvut.fit.steuejan.travel.api.app.location

import cz.cvut.fit.steuejan.travel.api.trip.model.GetTripsType
import io.ktor.locations.*
import org.joda.time.DateTime

@Location("${Trip.URL}/{id?}")
class Trip(val id: Int? = null) {

    @Location(Invite.URL)
    class Invite(val trip: Trip) {
        companion object {
            const val URL = "/invite"
        }
    }

    @Location("${Transport.URL}/{transportId?}")
    class Transport(val trip: Trip, val transportId: Int? = null) {
        companion object {
            const val URL = "/transport"
        }
    }

    @Location("${Accomodation.URL}/{accomodationId?}")
    class Accomodation(val trip: Trip, val accomodationId: Int? = null) {
        companion object {
            const val URL = "/accomodation"
        }
    }

    @Location("${Activity.URL}/{activityId?}")
    class Activity(val trip: Trip, val activityId: Int? = null) {
        companion object {
            const val URL = "/activity"
        }
    }

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