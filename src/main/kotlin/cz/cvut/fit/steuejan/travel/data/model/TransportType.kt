package cz.cvut.fit.steuejan.travel.data.model

@Suppress("unused") // used in DB
enum class TransportType {
    WALK, BIKE, CAR, BUS, TRAIN, FERRY, PUBLIC, PLANE;

    companion object {
        const val MAX_LENGTH = 6
    }
}