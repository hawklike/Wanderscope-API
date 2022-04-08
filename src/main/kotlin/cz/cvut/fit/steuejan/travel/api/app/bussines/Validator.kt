package cz.cvut.fit.steuejan.travel.api.app.bussines

import cz.cvut.fit.steuejan.travel.api.app.config.LimitsConfig
import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.auth.exception.EmailAlreadyExistsException
import cz.cvut.fit.steuejan.travel.api.auth.exception.UsernameAlreadyExistsException
import cz.cvut.fit.steuejan.travel.api.auth.model.AccountType
import cz.cvut.fit.steuejan.travel.data.database.user.dao.UserDao
import cz.cvut.fit.steuejan.travel.data.model.Username
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

    suspend fun validateUsername(username: Username) = with(config) {
        if (username.it.isBlank()) {
            throw BadRequestException(FailureMessages.isBlank("username"))
        }

        if (username.it.length !in (usernameLengthMin..usernameLengthMax)) {
            throw BadRequestException(FailureMessages.lengthIsBad("username", usernameLengthMin, usernameLengthMax))
        }

        if (userDao.findByUsername(username) != null) {
            throw UsernameAlreadyExistsException()
        }
    }
}