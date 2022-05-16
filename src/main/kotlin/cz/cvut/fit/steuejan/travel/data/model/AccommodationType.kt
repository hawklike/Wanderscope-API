package cz.cvut.fit.steuejan.travel.data.model

@Suppress("unused") // used in DB
enum class AccommodationType {
    HOTEL, HOSTEL, PENSION, CAMP, OUTDOOR, AIRBNB, OTHER;

    companion object {
        const val MAX_LENGTH = 7
    }
}