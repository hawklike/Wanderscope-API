package cz.cvut.fit.steuejan.travel.api.app.route

import cz.cvut.fit.steuejan.travel.api.app.extension.receive
import cz.cvut.fit.steuejan.travel.api.app.extension.respond
import cz.cvut.fit.steuejan.travel.api.app.request.Request
import cz.cvut.fit.steuejan.travel.api.app.response.general.Success
import cz.cvut.fit.steuejan.travel.api.auth.jwt.JWTConfig.Companion.JWT_AUTHENTICATION
import cz.cvut.fit.steuejan.travel.api.auth.jwt.UsernamePrincipal
import cz.cvut.fit.steuejan.travel.data.database.addUser
import cz.cvut.fit.steuejan.travel.data.database.createTrip
import cz.cvut.fit.steuejan.travel.data.database.deleteTrip
import cz.cvut.fit.steuejan.travel.data.database.getTrips
import cz.cvut.fit.steuejan.travel.data.model.Username
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*

@kotlinx.serialization.Serializable
data class User(val username: String, val email: String) : Request

@kotlinx.serialization.Serializable
data class Trip(val username: String, val name: String) : Request

@kotlinx.serialization.Serializable
data class Username(val username: String) : Request

@KtorExperimentalLocationsAPI
fun Route.exampleRoutes() {

    authenticate(JWT_AUTHENTICATION) {
        get("/test") {
            val principal = call.principal<UsernamePrincipal>()
            val expiresAt = principal?.principal?.expiresAt?.time?.minus(System.currentTimeMillis())?.div(1000.0)
            call.respond("User ${principal?.username} will expire in $expiresAt seconds.")
        }
    }

    post("/user") {
        with(receive<User>("")) {
            addUser(username, email)
        }
        respond(Success())
    }

    post("/trip") {
        with(receive<Trip>("")) {
            createTrip(name, Username(username))
        }
        respond(Success())
    }

    get("/trips") {
        val username = call.request.queryParameters["id"]!!
        respond(Success(message = getTrips(username.toInt())))
    }

    delete("/trip") {
        val id = call.request.queryParameters["id"]!!
        deleteTrip(id.toInt())
        respond(Success())
    }
}
