package cz.cvut.fit.steuejan.travel.data.database.document

import cz.cvut.fit.steuejan.travel.data.dto.Dto
import cz.cvut.fit.steuejan.travel.data.model.DocumentType
import org.jetbrains.exposed.sql.ResultRow
import org.joda.time.DateTime

data class DocumentDto(
    val id: Int,
    val userId: Int,
    val tripId: Int,
    val transportId: Int?,
    val placeId: Int?,
    val accomodationId: Int?,
    val activityId: Int?,
    val name: String,
    val updated: DateTime,
    val type: DocumentType,
    val key: String?
) : Dto() {
    companion object {
        fun fromDb(resultRow: ResultRow) = DocumentDto(
            id = resultRow[DocumentTable.id].value,
            userId = resultRow[DocumentTable.owner].value,
            tripId = resultRow[DocumentTable.trip].value,
            transportId = resultRow[DocumentTable.transport]?.value,
            placeId = resultRow[DocumentTable.place]?.value,
            accomodationId = resultRow[DocumentTable.accomodation]?.value,
            activityId = resultRow[DocumentTable.activity]?.value,
            name = resultRow[DocumentTable.name],
            updated = resultRow[DocumentTable.updated],
            type = resultRow[DocumentTable.type],
            key = resultRow[DocumentTable.key]
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DocumentDto

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}
