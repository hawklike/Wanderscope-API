package cz.cvut.fit.steuejan.travel.api.account.controller

import cz.cvut.fit.steuejan.travel.api.account.model.ChangePassword
import cz.cvut.fit.steuejan.travel.api.account.response.ChangePasswordResponse
import cz.cvut.fit.steuejan.travel.api.app.bussines.Validator
import cz.cvut.fit.steuejan.travel.api.app.di.factory.DaoFactory
import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.NotFoundException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.app.response.Status
import cz.cvut.fit.steuejan.travel.api.app.response.Success
import cz.cvut.fit.steuejan.travel.api.app.util.execOrNotFound
import cz.cvut.fit.steuejan.travel.api.auth.exception.PasswordChangeProhibitedException
import cz.cvut.fit.steuejan.travel.api.auth.jwt.JWTController
import cz.cvut.fit.steuejan.travel.api.auth.util.Encryptor
import cz.cvut.fit.steuejan.travel.data.model.EmailLogin

class AccountController(
    private val daoFactory: DaoFactory,
    private val encryptor: Encryptor,
    private val jwt: JWTController,
    private val validator: Validator
) {

    suspend fun logout(refreshToken: String): Response {
        daoFactory.tokenDao.deleteToken(refreshToken)
        return Success(Status.NO_CONTENT)
    }

    suspend fun logoutAllDevices(userId: Int): Response {
        daoFactory.tokenDao.deleteTokensByUserId(userId)
        return Success(Status.NO_CONTENT)
    }

    suspend fun delete(userId: Int): Response {
        if (!daoFactory.userDao.deleteUser(userId)) {
            throw NotFoundException(FailureMessages.USER_NOT_FOUND)
        }
        logoutAllDevices(userId)
        return Success(Status.NO_CONTENT)
    }

    suspend fun changePassword(userId: Int, passwordRequest: ChangePassword, addToDb: Boolean): Response {
        val newPassword = passwordRequest.newPassword
        val oldPassword = passwordRequest.oldPassword

        checkPasswordChange(passwordRequest)
        validator.validatePassword(newPassword)
        checkOldPassword(userId, oldPassword)

        if (!daoFactory.userDao.changePassword(userId, encryptor.hashPassword(newPassword))) {
            throw NotFoundException(FailureMessages.USER_NOT_FOUND)
        }

        logoutAllDevices(userId)
        val tokens = jwt.createTokens(userId, addToDb)
        return ChangePasswordResponse.success(tokens.accessToken, tokens.refreshToken)
    }

    private fun checkPasswordChange(passwordRequest: ChangePassword) {
        with(passwordRequest) {
            if (newPassword != confirmNewPassword) {
                throw BadRequestException(FailureMessages.PASSWORDS_DONT_MATCH)
            }

            if (newPassword == oldPassword) {
                throw BadRequestException(FailureMessages.PASSWORDS_ARE_SAME_ERROR)
            }
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
