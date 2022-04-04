package cz.cvut.fit.steuejan.travel.data.dto

import cz.cvut.fit.steuejan.travel.data.model.Duration

interface PointOfInterestDto {
    val id: Int
    val tripId: Int
    val duration: Duration
}