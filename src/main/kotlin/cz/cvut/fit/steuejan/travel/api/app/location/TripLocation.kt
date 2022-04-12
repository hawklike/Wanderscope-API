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

    @Location(Leave.URL)
    class Leave(val trip: Trip) {
        companion object {
            const val URL = "/leave"
        }
    }

    @Location(Date.URL)
    class Date(val trip: Trip) {
        companion object {
            const val URL = "/date"
        }
    }

    @Location(Documents.URL)
    class Documents(val trip: Trip) {
        companion object {
            const val URL = "/documents"
        }
    }

    @Location(Users.URL)
    class Users(val trip: Trip, val canEdit: Boolean? = null) {
        companion object {
            const val URL = "/users"
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

        @Location("${Document.URL}/{documentId?}")
        class Document(val transport: Transport, val documentId: Int? = null) {
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

        @Location(Documents.URL)
        class Documents(val transport: Transport) {
            companion object {
                const val URL = "/documents"
            }
        }
    }

    @Location("${Accomodation.URL}/{accomodationId?}")
    class Accomodation(val trip: Trip, val accomodationId: Int? = null) {
        companion object {
            const val URL = "/accommodation"
        }

        @Location("${Document.URL}/{documentId?}")
        class Document(val accommodation: Accomodation, val documentId: Int? = null) {
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

        @Location(Documents.URL)
        class Documents(val accommodation: Accomodation) {
            companion object {
                const val URL = "/documents"
            }
        }
    }

    @Location("${Activity.URL}/{activityId?}")
    class Activity(val trip: Trip, val activityId: Int? = null) {
        companion object {
            const val URL = "/activity"
        }

        @Location("${Document.URL}/{documentId?}")
        class Document(val activity: Activity, val documentId: Int? = null) {
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

        @Location(Documents.URL)
        class Documents(val activity: Activity) {
            companion object {
                const val URL = "/documents"
            }
        }
    }

    @Location("${Place.URL}/{placeId?}")
    class Place(val trip: Trip, val placeId: Int? = null) {
        companion object {
            const val URL = "/place"
        }

        @Location("${Document.URL}/{documentId?}")
        class Document(val place: Place, val documentId: Int? = null) {
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

        @Location(Documents.URL)
        class Documents(val place: Place) {
            companion object {
                const val URL = "/documents"
            }
        }
    }
}

@Location(Trips.URL)
class Trips(val scope: GetTripsType = GetTripsType.ALL, val date: DateTime? = null) {
    companion object {
        const val URL = "/trips"
    }
}