package cz.cvut.fit.steuejan.travel.api.trip.poi.place.wiki

import kotlinx.serialization.Serializable

@Serializable
data class Search(
    val title: String,
    val pageid: Int
)