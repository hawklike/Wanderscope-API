package cz.cvut.fit.steuejan.travel.api.account.controller

import cz.cvut.fit.steuejan.travel.api.account.model.ChangePassword
import cz.cvut.fit.steuejan.travel.api.account.response.ChangePasswordResponse
import cz.cvut.fit.steuejan.travel.api.account.response.LogoutResponse
import cz.cvut.fit.steuejan.travel.api.app.bussines.Validator
import cz.cvut.fit.steuejan.travel.api.app.di.factory.DaoFactory
import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.app.response.general.Response
import cz.cvut.fit.steuejan.travel.api.app.response.model.User
import cz.cvut.fit.steuejan.travel.api.app.util.execOrNotFound
import cz.cvut.fit.steuejan.travel.api.auth.controller.AuthAccount
import cz.cvut.fit.steuejan.travel.api.auth.exception.PasswordChangeProhibitedException
import cz.cvut.fit.steuejan.travel.api.auth.jwt.JWTController
import cz.cvut.fit.steuejan.travel.api.auth.util.Encryptor
import cz.cvut.fit.steuejan.travel.data.model.Username

class UserAccountController(
    private val daoFactory: DaoFactory,
    private val encryptor: Encryptor,
    private val jwt: JWTController,
    private val validator: Validator
) : AuthAccount {

    override fun logout(refreshToken: String): Response {
        daoFactory.tokenDao.deleteToken(refreshToken)
        return LogoutResponse.success()
    }

    override fun logoutAllDevices(username: Username): Response {
        return if (daoFactory.tokenDao.deleteTokensByUsername(username)) {
            LogoutResponse.success()
        } else {
            LogoutResponse.failed()
        }
    }

    override fun changePassword(username: Username, password: ChangePassword): Response = with(password) {
        checkPasswordChange(password)
        validator.validatePassword(newPassword)
        checkOldPassword(username, oldPassword)

        val userDto = execOrNotFound(FailureMessages.userNotFound(username.it)) {
            daoFactory.userDao.changePassword(username, encryptor.hashPassword(newPassword))
        }

        if (logoutAllDevices(username).isSuccess()) {
            val tokens = jwt.createTokens(username)
            ChangePasswordResponse.success(
                User.fromUserDto(userDto),
                tokens.accessToken,
                tokens.refreshToken
            )
        } else {
            ChangePasswordResponse.failure()
        }
    }

    private fun checkPasswordChange(password: ChangePassword) = with(password) {
        if (newPassword != confirmNewPassword) {
            throw BadRequestException(FailureMessages.PASSWORDS_DONT_MATCH)
        }

        if (newPassword == oldPassword) {
            throw BadRequestException(FailureMessages.PASSWORDS_ARE_SAME_ERROR)
        }
    }

    private fun checkOldPassword(username: Username, oldPassword: String?) {
        if (oldPassword == null) {
            return
        }

        val userDto = execOrNotFound(FailureMessages.userNotFound(username.it)) {
            daoFactory.userDao.findByUsername(username)
        }

        if (!encryptor.checkPassword(userDto.password, oldPassword)) {
            throw PasswordChangeProhibitedException()
        }
    }
}
