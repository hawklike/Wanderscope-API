package cz.cvut.fit.steuejan.travel.api.trip.document.model

data class FileWrapper(
    val originalName: String,
    val rawData: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FileWrapper

        if (originalName != other.originalName) return false
        if (!rawData.contentEquals(other.rawData)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = originalName.hashCode()
        result = 31 * result + rawData.contentHashCode()
        return result
    }
}
