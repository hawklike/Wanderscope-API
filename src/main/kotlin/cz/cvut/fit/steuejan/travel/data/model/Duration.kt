package cz.cvut.fit.steuejan.travel.data.model

import cz.cvut.fit.steuejan.travel.api.app.plugin.DateTimeSerializer
import kotlinx.serialization.Serializable
import org.joda.time.DateTime

@Serializable
data class Duration(
    @Serializable(with = DateTimeSerializer::class)
    val startDate: DateTime?,
    @Serializable(with = DateTimeSerializer::class)
    val endDate: DateTime?
)
