package cz.cvut.fit.steuejan.travel.api.trip.controller

import cz.cvut.fit.steuejan.travel.api.app.response.Response

interface TripController {
    fun createTrip(): Response
    fun deleteTrip(): Response
    fun shareTrip(): Response
}