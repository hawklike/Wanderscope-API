package cz.cvut.fit.steuejan.travel.data.model

import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages

enum class PointOfInterestType {
    TRANSPORT, ACCOMMODATION, ACTIVITY, PLACE;

    fun notFound() = when (this) {
        TRANSPORT -> FailureMessages.TRANSPORT_NOT_FOUND
        ACCOMMODATION -> FailureMessages.ACCOMMODATION_NOT_FOUND
        ACTIVITY -> FailureMessages.ACTIVITY_NOT_FOUND
        PLACE -> FailureMessages.PLACE_NOT_FOUND
    }
}