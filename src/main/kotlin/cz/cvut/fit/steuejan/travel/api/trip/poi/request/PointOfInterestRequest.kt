package cz.cvut.fit.steuejan.travel.api.trip.poi.request

import cz.cvut.fit.steuejan.travel.api.app.request.Request
import cz.cvut.fit.steuejan.travel.data.dto.PointOfInterestDto
import cz.cvut.fit.steuejan.travel.data.model.Duration

abstract class PointOfInterestRequest<T : PointOfInterestDto> : Request {
    abstract val name: String
    abstract val duration: Duration?

    abstract fun toDto(): T
}