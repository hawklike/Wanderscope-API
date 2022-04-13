package cz.cvut.fit.steuejan.travel.data.database.trip.dto

import cz.cvut.fit.steuejan.travel.data.database.tripuser.TripUserDto
import cz.cvut.fit.steuejan.travel.data.database.user.UserDto
import cz.cvut.fit.steuejan.travel.data.dto.Dto

data class TripUsersDto(
    val user: UserDto,
    val connection: TripUserDto
) : Dto()
