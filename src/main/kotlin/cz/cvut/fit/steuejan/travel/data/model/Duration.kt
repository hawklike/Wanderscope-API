package cz.cvut.fit.steuejan.travel.data.model

import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.app.plugin.DateTimeSerializer
import kotlinx.serialization.Serializable
import org.joda.time.DateTime

@Serializable
data class Duration(
    @Serializable(with = DateTimeSerializer::class)
    val startDate: DateTime? = null,
    @Serializable(with = DateTimeSerializer::class)
    val endDate: DateTime? = null
) {
    fun validate(): Duration {
        if (startDate != null && endDate != null) {
            if (endDate.isBefore(startDate)) {
                throw BadRequestException(FailureMessages.END_DATE_BEFORE_START_DATE_ERROR)
            }
        }
        return this
    }
}
