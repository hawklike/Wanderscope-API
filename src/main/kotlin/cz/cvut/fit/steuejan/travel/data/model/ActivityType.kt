package cz.cvut.fit.steuejan.travel.data.model

@Suppress("unused") // used in DB
enum class ActivityType {
    HIKING, CYCLING, SKIING, RUNNING, KAYAK, SWIMMING, CLIMBING, CROSS_COUNTRY;

    companion object {
        const val MAX_LENGTH = 13
    }
}