package cz.cvut.fit.steuejan.travel.api.trip.points.response

import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.app.response.Success
import kotlinx.serialization.Serializable

@Serializable
abstract class AbstractPointOfInterestResponse : Response by Success()