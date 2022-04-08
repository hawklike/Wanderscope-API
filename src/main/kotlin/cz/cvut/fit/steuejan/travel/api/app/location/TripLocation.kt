@file:OptIn(KtorExperimentalLocationsAPI::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package cz.cvut.fit.steuejan.travel.api.app.location

import cz.cvut.fit.steuejan.travel.api.trip.model.GetTripsType
import io.ktor.locations.*
import org.joda.time.DateTime

@Location("${Trip.URL}/{id?}")
class Trip(val id: Int? = null) {
    companion object {
        const val URL = "/trip"
    }

    @Location(Invite.URL)
    class Invite(val trip: Trip) {
        companion object {
            const val URL = "/invite"
        }
    }

    @Location(Date.URL)
    class Date(val trip: Trip) {
        companion object {
            const val URL = "/date"
        }
    }

    @Location("${Document.URL}/{documentId?}")
    class Document(val trip: Trip, val documentId: Int? = null) {
        companion object {
            const val URL = "/document"
        }

        @Location(Key.URL)
        class Key(val document: Document) {
            companion object {
                const val URL = "/key"
            }
        }

        @Location(Data.URL)
        class Data(val document: Document) {
            companion object {
                const val URL = "/data"
            }
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

    @Location("${Place.URL}/{placeId?}")
    class Place(val trip: Trip, val placeId: Int? = null) {
        companion object {
            const val URL = "/place"
        }
    }
}

@Location(Trips.URL)
class Trips(val scope: GetTripsType = GetTripsType.ALL, val date: DateTime? = null) {
    companion object {
        const val URL = "/trips"
    }
}