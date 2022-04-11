package cz.cvut.fit.steuejan.travel.api.app.route

import cz.cvut.fit.steuejan.travel.api.app.extension.getUserId
import cz.cvut.fit.steuejan.travel.api.app.extension.receive
import cz.cvut.fit.steuejan.travel.api.app.extension.respond
import cz.cvut.fit.steuejan.travel.api.app.plugin.DateTimeSerializer
import cz.cvut.fit.steuejan.travel.api.app.request.Request
import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.app.response.Success
import cz.cvut.fit.steuejan.travel.api.auth.jwt.JWTConfig.Companion.JWT_AUTHENTICATION
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.Serializable
import org.joda.time.DateTime

@Serializable
sealed class Common

@Serializable
data class User(val username: String, val email: String) : Common()

@Serializable
data class Username(val username: String) : Common()

@Serializable
data class Place(val placeId: String, val name: String) : Common()

@Serializable
data class AllResponse(val payload: List<Common>) : Response by Success()

@Serializable
data class Time(val YYYY: Int, val MM: Int, val dd: Int, val hh: Int, val mm: Int) : Request

@Serializable
data class TimeResponse(@Serializable(with = DateTimeSerializer::class) val time: DateTime) : Response by Success()

@KtorExperimentalLocationsAPI
fun Route.exampleRoutes() {

    authenticate(JWT_AUTHENTICATION) {
        get("/test") {
            val principal = call.principal<JWTPrincipal>()
            val userId = getUserId()
            val expiresAt = principal?.expiresAt?.time?.minus(System.currentTimeMillis())?.div(1000.0)
            call.respond("User $userId will expire in $expiresAt seconds.")
        }
    }

    post("/time") {
        with(receive<Time>("")) {
            respond(TimeResponse(DateTime(YYYY, MM, dd, hh, mm)))
        }
    }

    get("/version") {
        call.respondText("0.0.6")
    }

    get("/common") {
        val all = listOf(Username("honza"), User("jan", "jan@seznam.cz"), Place("id", "horni dolni"))
        respond(AllResponse(all))
    }
//
//    post("/user") {
//        with(receive<User>("")) {
//            addUser(username, email)
//        }
//        respond(Success())
//    }

//    get("/trips") {
//        val username = getQuery("id")
//        respond(Success(message = getTrips(username.toInt())))
//    }

//    delete("/trip") {
//        val id = getQuery("id")
//        deleteTrip(id.toInt())
//        respond(Success())
//    }

//    post("/place") {
//        val tripId = getQuery("tripId").toInt()
//        val place = receive<Place>("")
//        addPlace(tripId, place.placeId, place.name)
//        respond(Success())
//    }
//
//    get("/places") {
//        val tripId = getQuery("tripId").toInt()
//        respond(Success(message = getPlaces(tripId)))
//    }
//
//    delete("/place") {
//        val id = getQuery("id")
//        deletePlace(id.toInt())
//        respond(Success())
//    }
}
