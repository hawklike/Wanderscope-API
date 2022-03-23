package cz.cvut.fit.steuejan.travel.api.app.route

import cz.cvut.fit.steuejan.travel.api.app.extension.receive
import cz.cvut.fit.steuejan.travel.api.app.extension.respond
import cz.cvut.fit.steuejan.travel.api.app.request.Request
import cz.cvut.fit.steuejan.travel.api.app.response.general.Success
import cz.cvut.fit.steuejan.travel.api.auth.jwt.JWTConfig.Companion.JWT_AUTHENTICATION
import cz.cvut.fit.steuejan.travel.api.auth.jwt.UsernamePrincipal
import cz.cvut.fit.steuejan.travel.data.database.addCity
import cz.cvut.fit.steuejan.travel.data.database.getCities
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*

@kotlinx.serialization.Serializable
data class City(val name: String) : Request

@KtorExperimentalLocationsAPI
fun Route.exampleRoutes() {

    authenticate(JWT_AUTHENTICATION) {
        get("/test") {
            val principal = call.principal<UsernamePrincipal>()
            val expiresAt = principal?.principal?.expiresAt?.time?.minus(System.currentTimeMillis())?.div(1000.0)
            call.respond("User ${principal?.username} will expire in $expiresAt seconds.")
        }
    }

    post("/city") {
        val city = receive<City>("")
        addCity(city.name)
        respond(Success())
    }

    get("cities") {
        val cities = getCities()
        respond(Success(message = cities))
    }
}
