package cz.cvut.fit.steuejan.travel.data.dto

import cz.cvut.fit.steuejan.travel.data.database.trip.TripDto
import cz.cvut.fit.steuejan.travel.data.database.tripuser.TripUserDto

data class TripOverviewDto(
    val trip: TripDto,
    val tripUser: TripUserDto
) : Dto()
