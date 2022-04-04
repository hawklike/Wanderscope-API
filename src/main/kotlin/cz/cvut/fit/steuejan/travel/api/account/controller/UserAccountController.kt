package cz.cvut.fit.steuejan.travel.api.account.controller

import cz.cvut.fit.steuejan.travel.api.account.model.ChangePassword
import cz.cvut.fit.steuejan.travel.api.account.response.ChangePasswordResponse
import cz.cvut.fit.steuejan.travel.api.account.response.LogoutResponse
import cz.cvut.fit.steuejan.travel.api.app.bussines.Validator
import cz.cvut.fit.steuejan.travel.api.app.di.factory.DaoFactory
import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.NotFoundException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.app.response.general.Response
import cz.cvut.fit.steuejan.travel.api.app.util.execOrNotFound
import cz.cvut.fit.steuejan.travel.api.auth.controller.AuthAccount
import cz.cvut.fit.steuejan.travel.api.auth.exception.PasswordChangeProhibitedException
import cz.cvut.fit.steuejan.travel.api.auth.jwt.JWTController
import cz.cvut.fit.steuejan.travel.api.auth.util.Encryptor
import cz.cvut.fit.steuejan.travel.data.model.EmailLogin

class UserAccountController(
    private val daoFactory: DaoFactory,
    private val encryptor: Encryptor,
    private val jwt: JWTController,
    private val validator: Validator
) : AuthAccount {

    override suspend fun logout(refreshToken: String): Response {
        daoFactory.tokenDao.deleteToken(refreshToken)
        return LogoutResponse.success()
    }

    override suspend fun logoutAllDevices(userId: Int): Response {
        daoFactory.tokenDao.deleteTokensByUserId(userId)
        return LogoutResponse.success()
    }

    override suspend fun changePassword(userId: Int, password: ChangePassword): Response = with(password) {
        checkPasswordChange(password)
        validator.validatePassword(newPassword)
        checkOldPassword(userId, oldPassword)

        if (!daoFactory.userDao.changePassword(userId, encryptor.hashPassword(newPassword))) {
            throw NotFoundException(FailureMessages.USER_NOT_FOUND)
        }

        logoutAllDevices(userId)
        val tokens = jwt.createTokens(userId, addToDatabase = false)
        ChangePasswordResponse.success(tokens.accessToken, tokens.refreshToken)
    }

    private fun checkPasswordChange(password: ChangePassword) = with(password) {
        if (newPassword != confirmNewPassword) {
            throw BadRequestException(FailureMessages.PASSWORDS_DONT_MATCH)
        }

        if (newPassword == oldPassword) {
            throw BadRequestException(FailureMessages.PASSWORDS_ARE_SAME_ERROR)
        }
    }

    private suspend fun checkOldPassword(userId: Int, oldPassword: String?) {
        if (oldPassword == null) {
            return
        }

        val login = execOrNotFound(FailureMessages.USER_NOT_FOUND) {
            daoFactory.userDao.findById(userId)?.credentials?.login as EmailLogin
        }

        if (!encryptor.checkPassword(login.password, oldPassword)) {
            throw PasswordChangeProhibitedException()
        }
    }
}
