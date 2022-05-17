package cz.cvut.fit.steuejan.travel.api.trip.poi.place.wiki

import kotlinx.coroutines.CoroutineScope

data class WikiSearchBundle(
    val scope: CoroutineScope,
    val searchTerm: String
)