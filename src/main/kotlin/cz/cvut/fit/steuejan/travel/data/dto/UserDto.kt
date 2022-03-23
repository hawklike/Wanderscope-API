package cz.cvut.fit.steuejan.travel.data.dto

import cz.cvut.fit.steuejan.travel.data.model.Username

//todo use Credentials instead
data class UserDto(val username: Username, val account: String, var password: String?)