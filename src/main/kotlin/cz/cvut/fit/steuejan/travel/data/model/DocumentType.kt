package cz.cvut.fit.steuejan.travel.data.model

@Suppress("unused") // used in DB
enum class DocumentType {
    TEXT, IMAGE, DOCUMENT, GPX;

    companion object {
        const val MAX_LENGTH = 8
    }
}