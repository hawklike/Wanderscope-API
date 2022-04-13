package cz.cvut.fit.steuejan.travel.api.app.exception.message

import cz.cvut.fit.steuejan.travel.api.app.bussines.Validator
import cz.cvut.fit.steuejan.travel.api.app.route.DOCUMENT_KEY_HEADER
import cz.cvut.fit.steuejan.travel.data.config.DatabaseConfig.Companion.NAME_LENGTH

object FailureMessages {
    const val EMAIL_ALREADY_EXISTS = "Account with this email already exists."
    const val USERNAME_ALREADY_EXISTS = "This username already exists."
    const val EMAIL_PASSWORD_INCORRECT = "Email or password is incorrect."

    const val REFRESH_TOKEN_INVALID = "Refresh token is invalid."
    const val REFRESH_TOKEN_EXPIRED = "Refresh token has expired."

    const val PASSWORDS_DONT_MATCH = "Passwords don't match."
    const val PASSWORDS_ARE_SAME_ERROR = "New password is the same as the old one."
    const val OLD_PASSWORD_WRONG = "Given password is wrong."
    const val RESET_PASSWORD_MISSING_PASS = "Missing password parameter."
    const val RESET_PASSWORD_MISSING_COFIRM_PASS = "Missing password confirmation parameter."
    const val RESET_PASSWORD_PROHIBITED = "Resetting password prohibited."

    const val EMAIL_ADDRESS_BAD_FORMAT = "Email address has an invalid format."

    const val JWT_SUB_MISSING = "Parameter 'sub' is missing or does not represent user id."
    const val UNAUTHORIZED =
        "User is not authorized. Provide valid JWT as the bearer token in the Authorization header."

    const val CONVERSION_ERROR =
        "Check if your request URL is not missing any resource identification. For example if you didn't send foo/bar instead of foo/12/bar."

    const val USER_NOT_FOUND = "User not found."
    const val TRIP_NOT_FOUND = "Trip not found."
    const val USER_OR_TRIP_NOT_FOUND =
        "Cannot add user to the trip. User or trip not found or user is already a member of the trip."
    const val USER_TRIP_NOT_FOUND = "User is not a member of this trip."

    const val TRANSPORT_NOT_FOUND = "Transport not found."
    const val ACCOMODATION_NOT_FOUND = "Accomodation not found."
    const val ACTIVITY_NOT_FOUND = "Activity not found."
    const val PLACE_NOT_FOUND = "Place not found."
    const val DOCUMENT_NOT_FOUND = "Document not found."

    const val ADD_USER_FAILURE = "Cannot add this user. Check duplicates."
    const val ADD_TRIP_FAILURE = "Cannot add this trip. Check name length and if owner exists."
    const val ADD_DOCUMENT_METADATA_FAILURE =
        "Cannot add this document. Check name length, extension length and if both owner and trip exist."
    const val ADD_DOCUMENT_METADATA_POI_NULL =
        "Point of interest aka transport, accommodation, activity or place cannot be null."
    const val ADD_DOCUMENT_DATA_FAILURE = "Saving document to database failed: "

    const val NAME_TOO_LONG = "Name is too long. Max length allowed is $NAME_LENGTH"

    const val DELETE_TRIP_PROHIBITED = "User doesn't have an admin role in this trip."
    const val EDIT_TRIP_PROHIBITED = "User cannot edit this trip."
    const val EDIT_CHANGE_ROLE_PROHIBITED = "Only admins in this trip may change users role."

    const val INVITE_PROHIBITED = "User cannot invite in this trip."
    const val INVITE_EDITOR_PROHIBITED = "Editor cannot invite new user who will have admin rights."

    const val LEAVE_TRIP_PROHIBITED =
        "User can't leave this trip. He/she must be alone in the trip or there must be another admin within the trip."
    const val CHANGE_ROLE_TO_MYSELF_PROHIBITED = "At least one admin must stay in this trip."

    const val DOCUMENT_UPLOAD_PROHIBITED = "Only owner of the document may upload data."
    const val DOCUMENT_SET_KEY_PROHIBITED = "Only owner of the document may set a key."
    const val DOCUMENT_DATA_PROHIBITED =
        "Document is secured with a key. Provide correct key using custom header $DOCUMENT_KEY_HEADER"
    const val DOCUMENT_DATA_NULL = "Document content not provided, nothing to see."

    const val MULTIPART_FORM_MISSING_FILE = "Missing file part in the multipart form data."
    const val MULTIPART_FORM_MISSING_FILE_NAME = "Missing file name."
    const val MULTIPART_FORM_FILE_EXTENSION_PROHIBITED =
        "File with this extension is not permitted. Permitted files are ${Validator.ALLOWED_EXTENSIONS}."

    fun missingQueryParam(param: String): String {
        return "Missing query parameter '$param'."
    }

    fun poiDbInsertionFailure(poiName: String): String {
        return "Cannot add this $poiName point. Check name length and if trip exists."
    }

    @Suppress("DEPRECATION")
    fun lengthIsBad(what: String, minLength: Int, maxLength: Int): String {
        return "${what.capitalize()} should be min of $minLength and max of $maxLength characters long."
    }

    @Suppress("DEPRECATION")
    fun isBlank(what: String): String {
        return "${what.capitalize()} cannot be blank."
    }

    fun documentMaxSize(maxSizeInMb: Int): String {
        return "Document is too big. Max allowed size of a document is ${maxSizeInMb / 1_000_000} MB."
    }
}