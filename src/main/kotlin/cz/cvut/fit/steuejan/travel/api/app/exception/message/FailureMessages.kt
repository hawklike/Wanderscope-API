package cz.cvut.fit.steuejan.travel.api.app.exception.message

object FailureMessages {
    const val PASSWORDS_DONT_MATCH = "Passwords don't match."
    const val PASSWORDS_ARE_SAME_ERROR = "New password is the same as the old one."
    const val PASSWORD_CHANGE_ERROR = "Password change failed."

    const val JWT_SUB_MISSING = "Parameter 'sub' is missing or does not represent user id."
    const val ALL_DEVICES_LOGOUT_ERROR = "Cannot logout this user from all devices. This user doesn't exist."

    const val RESET_PASSWORD_MISSING_PASS = "Missing password parameter."
    const val RESET_PASSWORD_MISSING_COFIRM_PASS = "Missing password confirmation parameter."
    const val RESET_PASSWORD_PROHIBITED = "Resetting password prohibited."

    const val USER_NOT_FOUND = "User not found."
    const val TRIP_NOT_FOUND = "Trip not found."

    const val ADD_USER_FAILURE = "Cannot add this user. Check duplicates."
    const val ADD_TRIP_FAILURE = "Cannot add this trip. Check name length."


    const val ADD_REFRESH_TOKEN_FAILURE = "Cannot add a new refresh token because of a duplicate."

    fun missingQueryParam(param: String) = "Missing query parameter $param."
}