package cz.cvut.fit.steuejan.travel.api.app.exception.message

import cz.cvut.fit.steuejan.travel.data.config.DatabaseConfig

object FailureMessages {
    const val PASSWORDS_DONT_MATCH = "Passwords don't match."
    const val PASSWORDS_ARE_SAME_ERROR = "New password is the same as the old one."
    const val RESET_PASSWORD_MISSING_PASS = "Missing password parameter."
    const val RESET_PASSWORD_MISSING_COFIRM_PASS = "Missing password confirmation parameter."
    const val RESET_PASSWORD_PROHIBITED = "Resetting password prohibited."

    const val JWT_SUB_MISSING = "Parameter 'sub' is missing or does not represent user id."

    const val USER_NOT_FOUND = "User not found."
    const val TRIP_NOT_FOUND = "Trip not found."
    const val USER_OR_TRIP_NOT_FOUND =
        "Cannot add user to the trip. User or trip not found or user is a already a member of the trip."
    const val USER_TRIP_NOT_FOUND = "User is not a member of this trip."

    const val ADD_USER_FAILURE = "Cannot add this user. Check duplicates."
    const val ADD_TRIP_FAILURE = "Cannot add this trip. Check name length."

    const val NAME_TOO_LONG = "Name is too long. Max length allowed is ${DatabaseConfig.NAME_LENGTH}"

    const val DELETE_TRIP_PROHIBITED = "User is not owner of the trip."
    const val USER_ACTION_PROHIBITED = "User's action is prohibited due to weak permissions."

    const val SHOW_TRIPS_MISSING_DATE = "Query parameter 'date' is mandatory."

    fun missingQueryParam(param: String) = "Missing query parameter $param."
}