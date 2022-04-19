package cz.cvut.fit.steuejan.travel.api.app.exception.message

import cz.cvut.fit.steuejan.travel.api.app.bussines.Validator
import cz.cvut.fit.steuejan.travel.api.app.route.DOCUMENT_KEY_HEADER
import cz.cvut.fit.steuejan.travel.data.config.DatabaseConfig.Companion.CURRENCY_CODE_LENGTH
import cz.cvut.fit.steuejan.travel.data.config.DatabaseConfig.Companion.NAME_LENGTH

object FailureMessages {
    const val EMAIL_ALREADY_EXISTS = "An account with this email already exists."
    const val USERNAME_ALREADY_EXISTS = "This username already exists."
    const val EMAIL_PASSWORD_INCORRECT = "The email or password is incorrect."

    const val REFRESH_TOKEN_INVALID = "The refresh token is invalid."
    const val REFRESH_TOKEN_EXPIRED = "The refresh token has expired."

    const val END_DATE_BEFORE_START_DATE_ERROR =
        "The time of the 'endDate' parameter cannot be before the time of the 'startDate' parameter."

    const val PASSWORDS_DONT_MATCH = "Passwords don't match."
    const val PASSWORDS_ARE_SAME_ERROR = "The new password is the same as the old one."
    const val OLD_PASSWORD_WRONG = "The entered password is wrong."
    const val RESET_PASSWORD_MISSING_PASS = "Missing password parameter."
    const val RESET_PASSWORD_MISSING_COFIRM_PASS = "Missing password confirmation parameter."
    const val RESET_PASSWORD_PROHIBITED = "Resetting a password is prohibited."

    const val EMAIL_ADDRESS_BAD_FORMAT = "The email address has an invalid format."

    const val JWT_SUB_MISSING = "Parameter 'sub' is missing or does not represent user id."
    const val UNAUTHORIZED =
        "The user is not authorized. Provide valid JWT as the bearer token in the Authorization header."

    const val CONVERSION_ERROR =
        "Check if your request URL is not missing any resource identification. For example, if you didn't send foo/bar instead of foo/12/bar."

    const val USER_NOT_FOUND = "User not found."
    const val TRIP_NOT_FOUND = "Trip not found."
    const val USER_OR_TRIP_NOT_FOUND =
        "Cannot add a user to the trip. User or trip not found or user is already a member of the trip."
    const val USER_TRIP_NOT_FOUND = "The user is not a member of this trip."

    const val TRANSPORT_NOT_FOUND = "Transport not found."
    const val ACCOMMODATION_NOT_FOUND = "Accommodation not found."
    const val ACTIVITY_NOT_FOUND = "Activity not found."
    const val PLACE_NOT_FOUND = "Place not found."
    const val DOCUMENT_NOT_FOUND = "Document not found."
    const val EXPENSE_ROOM_NOT_FOUND = "Expense room not found."
    const val EXPENSE_NOT_FOUND = "Expense not found."

    const val ADD_USER_FAILURE = "Cannot add this user. Check duplicates."
    const val ADD_TRIP_FAILURE = "Cannot add this trip. Check name length and if the owner exists."
    const val ADD_DOCUMENT_METADATA_FAILURE =
        "Cannot add this document. Check name length, extension length, and if both owner and trip exist."
    const val ADD_DOCUMENT_METADATA_POI_NULL =
        "Point of interest aka transport, accommodation, activity, or place cannot be null."

    const val CURRENCY_TOO_lONG = "The currency code is too long. Max length allowed is $CURRENCY_CODE_LENGTH."
    const val ADD_EXPENSE_ROOM_FAILURE = "Cannot add this room. Check if the trip exists."
    const val EXPENSE_PERSONS_NOT_UNIQUE = "Person's names are not unique but should be."

    const val ADD_EXPENSE_FAILURE = "Cannot add the expense. Check if both the room and the trip exist."

    const val NAME_TOO_LONG = "The name is too long. Max length allowed is $NAME_LENGTH."

    const val DELETE_TRIP_PROHIBITED = "The user doesn't have an admin role on this trip."
    const val EDIT_TRIP_PROHIBITED = "The user cannot edit this trip."
    const val EDIT_CHANGE_ROLE_PROHIBITED = "Only admins on this trip may change the user's role."

    const val INVITE_PROHIBITED = "The user on this trip cannot invite anyone."
    const val INVITE_EDITOR_PROHIBITED = "An editor cannot invite a new user who will have admin rights."

    const val LEAVE_TRIP_PROHIBITED =
        "The user can't leave this trip. He/she must be alone on the trip or there must be another admin within the trip."
    const val CHANGE_ROLE_TO_MYSELF_PROHIBITED = "At least one admin must stay on this trip."

    const val DOCUMENT_UPLOAD_PROHIBITED = "Only the owner of a document may upload data."
    const val DOCUMENT_DELETE_PROHIBITED = "Not allowed to delete this document."
    const val DOCUMENT_UPLOAD_FAILED = "Document upload failed."
    const val DOCUMENT_DELETE_FAILED = "Document deletion failed."
    const val DOCUMENT_SET_KEY_PROHIBITED = "Only the owner of a document may set a key."
    const val DOCUMENT_DATA_PROHIBITED =
        "This document is secured with a key. Provide correct key using a custom header $DOCUMENT_KEY_HEADER"
    const val DOCUMENT_DATA_NULL = "Document content not provided, nothing to see."

    const val MULTIPART_FORM_MISSING_FILE = "Missing file part in the multipart form data."
    const val MULTIPART_FORM_MISSING_FILE_NAME = "Missing file name."
    const val MULTIPART_FORM_FILE_EXTENSION_PROHIBITED =
        "File with this extension is not permitted. Permitted files are ${Validator.ALLOWED_EXTENSIONS}."

    fun missingQueryParam(param: String): String {
        return "Missing query parameter '$param'."
    }

    fun poiDbInsertionFailure(poiName: String): String {
        return "Cannot add this $poiName point. Check name length and if the trip exists."
    }

    @Suppress("DEPRECATION")
    fun lengthIsBad(what: String, minLength: Int, maxLength: Int): String {
        return "${what.capitalize()} should be a minimum of $minLength and a maximum of $maxLength characters long."
    }

    @Suppress("DEPRECATION")
    fun isBlank(what: String): String {
        return "${what.capitalize()} cannot be blank."
    }

    fun documentMaxSize(maxSizeInMb: Int): String {
        return "The document is too big. Max allowed size of a document is ${maxSizeInMb / 1_000_000} MB."
    }

    @Suppress("DEPRECATION")
    fun illegalName(what: String): String {
        return "${what.capitalize()} cannot contain newline, ~ and ; characters."
    }

    fun expensePersonNotFound(vararg person: String): String {
        return "${person.joinToString()} not found."
    }
}