package cz.cvut.fit.steuejan.travel.data.model

@Suppress("unused") // used in DB
enum class PlaceType {
    PARKING, FOOD, NATURE, OTHER;

    companion object {
        const val MAX_LENGTH = 7
    }
}