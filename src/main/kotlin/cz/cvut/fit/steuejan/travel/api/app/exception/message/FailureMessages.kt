package cz.cvut.fit.steuejan.travel.api.app.exception.message

object FailureMessages {
    const val PASSWORDS_DONT_MATCH = "Password and password confirmation don't match."
    const val PASSWORDS_ARE_SAME_ERROR = "New password is the same as the old."
    const val PASSWORD_CHANGE_ERROR = "Password change failed."

    const val JWT_SUB_MISSING = "Parameter 'sub' is missing."
    const val ALL_DEVICES_LOGOUT_ERROR = "Cannot logout this user from all devices. This user doesn't exist."

    const val RESET_PASSWORD_MISSING_PASS = "Missing password parameter."
    const val RESET_PASSWORD_MISSING_COFIRM_PASS = "Missing password confirmation parameter."
    const val RESET_PASSWORD_PROHIBITED = "Resetting password prohibited."

    fun userNotFound(username: String) = "User $username not found."
}