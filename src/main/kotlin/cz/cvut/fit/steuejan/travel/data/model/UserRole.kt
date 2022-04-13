package cz.cvut.fit.steuejan.travel.data.model

@Suppress("unused") //used in DB
enum class UserRole {
    ADMIN, EDITOR, VIEWER;

    fun canEdit() = this in listOf(ADMIN, EDITOR)
    fun canView() = true

    companion object {
        const val MAX_LENGTH = 6
    }
}