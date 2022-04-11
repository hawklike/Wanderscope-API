package cz.cvut.fit.steuejan.travel.api.trip.document.model

import cz.cvut.fit.steuejan.travel.data.model.DocumentType

data class DocumentMetadata(
    val name: String,
    val type: DocumentType,
    val key: String?
)
