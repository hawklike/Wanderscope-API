package cz.cvut.fit.steuejan.travel.api.trip.poi.response

import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.app.response.Success
import cz.cvut.fit.steuejan.travel.data.model.Duration
import kotlinx.serialization.Serializable

@Serializable
abstract class AbstractPointOfInterestResponse : Response by Success() {
    abstract val id: Int
    abstract val tripId: Int
    abstract val duration: Duration
    abstract val name: String
}