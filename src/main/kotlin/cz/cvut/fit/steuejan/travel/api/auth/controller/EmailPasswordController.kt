package cz.cvut.fit.steuejan.travel.api.auth.controller

import cz.cvut.fit.steuejan.travel.api.account.model.ChangePassword
import cz.cvut.fit.steuejan.travel.api.app.baseUrl
import cz.cvut.fit.steuejan.travel.api.app.bussines.EmailSender
import cz.cvut.fit.steuejan.travel.api.app.bussines.Validator
import cz.cvut.fit.steuejan.travel.api.app.di.factory.DaoFactory
import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.app.location.Auth
import cz.cvut.fit.steuejan.travel.api.app.response.general.Failure
import cz.cvut.fit.steuejan.travel.api.app.response.general.Response
import cz.cvut.fit.steuejan.travel.api.app.response.general.Status
import cz.cvut.fit.steuejan.travel.api.app.response.general.Success
import cz.cvut.fit.steuejan.travel.api.app.util.getRandomString
import cz.cvut.fit.steuejan.travel.api.app.util.isExpired
import cz.cvut.fit.steuejan.travel.api.auth.exception.InvalidLoginException
import cz.cvut.fit.steuejan.travel.api.auth.jwt.JWTController
import cz.cvut.fit.steuejan.travel.api.auth.response.AuthResponse
import cz.cvut.fit.steuejan.travel.api.auth.util.Encryptor
import cz.cvut.fit.steuejan.travel.data.model.Credentials
import cz.cvut.fit.steuejan.travel.data.model.EmailLogin
import cz.cvut.fit.steuejan.travel.data.model.Username
import io.ktor.locations.*
import java.util.*
import kotlin.time.Duration.Companion.minutes

class EmailPasswordController(
    private val daoFactory: DaoFactory,
    private val jwt: JWTController,
    private val encryptor: Encryptor,
    private val validator: Validator,
    private val account: AuthAccount
) : AuthController<EmailLogin> {

    override fun register(credentials: Credentials<EmailLogin>): AuthResponse {
        val login = credentials.login

        with(validator) {
            validatePassword(login.password)
            validateEmail(login.account, register = true)
            validateUsername(credentials.username)
        }

        val passwordHash = encryptor.hashPassword(login.password)
        val user = daoFactory.userDao.addUser(credentials.username, login.account, passwordHash)

        val tokens = jwt.createTokens(user.username)
        return AuthResponse.success(tokens.accessToken, tokens.refreshToken)
    }

    override fun login(login: EmailLogin): AuthResponse {
        val email = login.account

        with(validator) {
            validateEmail(email, register = false)
            validatePassword(login.password)
        }

        val user = daoFactory.userDao.findByAccount(email) ?: throw InvalidLoginException()

        if (!encryptor.checkPassword(user.password, login.password)) {
            throw InvalidLoginException()
        }

        val tokens = jwt.createTokens(user.username)
        return AuthResponse.success(tokens.accessToken, tokens.refreshToken)
    }

    suspend fun forgotPassword(email: String, emailSender: EmailSender): Response {
        validator.validateEmail(email, register = false)

        val userDto = daoFactory.userDao.findByAccount(email) ?: return Success(Status.ACCEPTED)

        val randomToken = getRandomString(24)
        val hashedToken = encryptor.hash(randomToken)

        val expiresAt = Date(System.currentTimeMillis() + FORGOT_PASSWORD_EXPIRATION)
        daoFactory.forgotPasswordDao.addForgotPassword(hashedToken, expiresAt, userDto.username)

        emailSender.sendEmailAsync(email, SUBJECT, getEmailMessage(userDto.username, randomToken))

        return Success(Status.ACCEPTED)
    }

    fun resetPassword(token: String, password: ChangePassword): Response {
        val dao = daoFactory.forgotPasswordDao
        val hashedToken = encryptor.hash(token)

        val passwordResetDto = dao.getForgotPassword(hashedToken)
            ?: return Failure(Status.FORBIDDEN, FailureMessages.RESET_PASSWORD_PROHIBITED)

        if (passwordResetDto.expiresAt.isExpired()) {
            dao.deleteForgotPassword(hashedToken)
            return Failure(Status.FORBIDDEN, FailureMessages.RESET_PASSWORD_PROHIBITED)
        }

        return try {
            val response = account.changePassword(passwordResetDto.username, password)
            dao.deleteForgotPassword(hashedToken)
            response
        } catch (ex: BadRequestException) {
            Failure(Status.BAD_REQUEST, ex.message)
        }
    }

    @OptIn(KtorExperimentalLocationsAPI::class)
    private fun getEmailMessage(username: Username, token: String): String {
        return """
                Hi ${username.it},
                
                You can reset your password by clicking here: 
                $baseUrl${Auth.URL}${Auth.ForgotPassword.URL}${Auth.ForgotPassword.Create.URL}?token=$token
                
                Please don't reply to this email.
                
                Sincerely,
                Wanderscope team
            """.trimIndent()
    }

    companion object {
        private val FORGOT_PASSWORD_EXPIRATION = 60.minutes.inWholeMilliseconds
        private const val SUBJECT = "Create new password"
    }
}