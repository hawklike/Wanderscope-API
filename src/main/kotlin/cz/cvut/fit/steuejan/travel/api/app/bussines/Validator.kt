package cz.cvut.fit.steuejan.travel.api.app.bussines

import cz.cvut.fit.steuejan.travel.api.app.config.LimitsConfig
import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.auth.exception.EmailAlreadyExistsException
import cz.cvut.fit.steuejan.travel.api.auth.exception.UsernameAlreadyExistsException
import cz.cvut.fit.steuejan.travel.api.auth.model.AccountType
import cz.cvut.fit.steuejan.travel.data.database.user.dao.UserDao
import cz.cvut.fit.steuejan.travel.data.model.Username
import org.apache.commons.validator.routines.EmailValidator

class Validator(private val userDao: UserDao, private val config: LimitsConfig) {

    fun validatePassword(password: String) = with(config) {
        if (password.isBlank()) {
            throw BadRequestException("Password cannot be blank.")
        }

        if (password.length !in (passwordLengthMin..passwordLengthMax)) {
            throw BadRequestException("Password should be min of $passwordLengthMin and max of $passwordLengthMax characters long.")
        }
    }

    suspend fun validateEmail(email: String, register: Boolean) {
        if (!EmailValidator.getInstance().isValid(email)) {
            throw BadRequestException("Email address has an invalid format.")
        }

        if (register && userDao.findByEmail(email, AccountType.EMAIL) != null) {
            throw EmailAlreadyExistsException()
        }
    }

    suspend fun validateUsername(username: Username) = with(config) {
        if (username.it.isBlank()) {
            throw BadRequestException("Username cannot be blank.")
        }

        if (username.it.length !in (usernameLengthMin..usernameLengthMax)) {
            throw BadRequestException("Username should be min of $usernameLengthMin and max of $usernameLengthMax characters long.")
        }

        if (userDao.findByUsername(username) != null) {
            throw UsernameAlreadyExistsException()
        }
    }
}