package cz.cvut.fit.steuejan.travel.data.model

@Suppress("unused") // used in DB
enum class PlaceType {
    PARKING, RESTAURANT, COUNTRYSIDE, CITY, MUSEUM, ZOO, PARK, MONUMENT, MOUNTAINS, CASTLE, OTHER;

    companion object {
        const val MAX_LENGTH = 24
    }
}