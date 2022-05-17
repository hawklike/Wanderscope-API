package cz.cvut.fit.steuejan.travel.api.trip.poi.place.wiki

import kotlinx.serialization.Serializable

@Serializable
data class Query(
    val search: List<Search>
)