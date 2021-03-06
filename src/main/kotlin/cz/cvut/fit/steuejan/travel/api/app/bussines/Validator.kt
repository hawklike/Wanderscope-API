package cz.cvut.fit.steuejan.travel.api.app.bussines

import cz.cvut.fit.steuejan.travel.api.app.config.LimitsConfig
import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.app.extension.isNameAllowed
import cz.cvut.fit.steuejan.travel.api.auth.exception.EmailAlreadyExistsException
import cz.cvut.fit.steuejan.travel.api.auth.exception.UsernameAlreadyExistsException
import cz.cvut.fit.steuejan.travel.api.auth.model.AccountType
import cz.cvut.fit.steuejan.travel.api.trip.document.model.FileWrapper
import cz.cvut.fit.steuejan.travel.data.database.user.dao.UserDao
import cz.cvut.fit.steuejan.travel.data.model.Username
import org.apache.commons.io.FilenameUtils
import org.apache.commons.validator.routines.EmailValidator

class Validator(private val userDao: UserDao, private val config: LimitsConfig) {

    fun validatePassword(password: String, what: String = "password") = with(config) {
        if (password.isBlank()) {
            throw BadRequestException(FailureMessages.isBlank(what))
        }

        if (password.length !in (passwordLengthMin..passwordLengthMax)) {
            throw BadRequestException(FailureMessages.lengthIsBad(what, passwordLengthMin, passwordLengthMax))
        }
    }

    fun validateDocumentKey(key: String, what: String = "key") = with(config) {
        if (key.isBlank()) {
            throw BadRequestException(FailureMessages.isBlank(what))
        }

        if (key.length !in (documentKeyMin..documentKeyMax)) {
            throw BadRequestException(FailureMessages.lengthIsBad(what, documentKeyMin, documentKeyMax))
        }
    }

    suspend fun validateEmail(email: String, register: Boolean) {
        if (!EmailValidator.getInstance().isValid(email)) {
            throw BadRequestException(FailureMessages.EMAIL_ADDRESS_BAD_FORMAT)
        }

        if (register && userDao.findByEmail(email, AccountType.EMAIL) != null) {
            throw EmailAlreadyExistsException()
        }
    }

    suspend fun validateName(
        name: String,
        what: String,
        dbLookup: Boolean = false,
        minLength: Int = config.usernameLengthMin
    ) {
        if (name.isBlank()) {
            throw BadRequestException(FailureMessages.isBlank(what))
        }

        with(config) {
            if (name.length !in (minLength..usernameLengthMax)) {
                throw BadRequestException(FailureMessages.lengthIsBad(what, usernameLengthMin, usernameLengthMax))
            }
        }

        if (!name.isNameAllowed()) {
            throw BadRequestException(FailureMessages.illegalName(what))
        }

        if (dbLookup && userDao.findByUsername(Username(name)) != null) {
            throw UsernameAlreadyExistsException()
        }
    }

    fun validateExtension(originalName: String): String {
        try {
            val extension = FilenameUtils.getExtension(originalName).lowercase()
            if (extension !in ALLOWED_EXTENSIONS.split(", ")) {
                throw Exception()
            }
            return extension
        } catch (ex: Exception) {
            throw BadRequestException(FailureMessages.MULTIPART_FORM_FILE_EXTENSION_PROHIBITED)
        }
    }

    fun validateFileSize(file: FileWrapper) {
        if (file.rawData.size > config.documentMaxSize) {
            throw BadRequestException(FailureMessages.documentMaxSize(config.documentMaxSize))
        }
    }

    companion object {
        const val ALLOWED_EXTENSIONS = "jpg, jpeg, gif, png, txt, pdf, gpx"
    }
}