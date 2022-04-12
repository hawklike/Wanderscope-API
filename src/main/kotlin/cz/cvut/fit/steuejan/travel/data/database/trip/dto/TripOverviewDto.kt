package cz.cvut.fit.steuejan.travel.data.database.trip.dto

import cz.cvut.fit.steuejan.travel.data.database.tripuser.TripUserDto
import cz.cvut.fit.steuejan.travel.data.dto.Dto

data class TripOverviewDto(
    val trip: TripDto,
    val tripUser: TripUserDto
) : Dto()
