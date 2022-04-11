package cz.cvut.fit.steuejan.travel.data.model

@Suppress("unused") // used in DB
enum class AccomodationType {
    HOTEL, HOSTEL, PENSION, CAMP, OUTDOOR, AIRBNB;

    companion object {
        const val MAX_LENGTH = 7
    }
}