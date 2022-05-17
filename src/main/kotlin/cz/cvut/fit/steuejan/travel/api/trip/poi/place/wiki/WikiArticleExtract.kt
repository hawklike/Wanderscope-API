package cz.cvut.fit.steuejan.travel.api.trip.poi.place.wiki

import kotlinx.serialization.Serializable

@Serializable
data class WikiArticleExtract(
    val extract: String
)