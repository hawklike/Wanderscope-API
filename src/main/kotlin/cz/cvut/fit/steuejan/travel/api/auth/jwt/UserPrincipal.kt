package cz.cvut.fit.steuejan.travel.api.auth.jwt

import cz.cvut.fit.steuejan.travel.data.model.Username
import io.ktor.auth.*
import io.ktor.auth.jwt.*

class UsernamePrincipal(val username: Username, val principal: JWTPrincipal) : Principal