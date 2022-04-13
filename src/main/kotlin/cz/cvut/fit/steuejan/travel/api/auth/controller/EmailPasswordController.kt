package cz.cvut.fit.steuejan.travel.api.auth.controller

import cz.cvut.fit.steuejan.travel.api.account.controller.AccountController
import cz.cvut.fit.steuejan.travel.api.account.model.ChangePassword
import cz.cvut.fit.steuejan.travel.api.app.bussines.EmailSender
import cz.cvut.fit.steuejan.travel.api.app.bussines.Validator
import cz.cvut.fit.steuejan.travel.api.app.config.DeploymentConfig
import cz.cvut.fit.steuejan.travel.api.app.di.factory.DaoFactory
import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.app.location.Auth
import cz.cvut.fit.steuejan.travel.api.app.response.Failure
import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.app.response.Status
import cz.cvut.fit.steuejan.travel.api.app.response.Success
import cz.cvut.fit.steuejan.travel.api.app.util.getRandomString
import cz.cvut.fit.steuejan.travel.api.app.util.isExpired
import cz.cvut.fit.steuejan.travel.api.app.util.retryOnError
import cz.cvut.fit.steuejan.travel.api.auth.exception.InvalidLoginException
import cz.cvut.fit.steuejan.travel.api.auth.jwt.JWTController
import cz.cvut.fit.steuejan.travel.api.auth.model.AccountType
import cz.cvut.fit.steuejan.travel.api.auth.response.AuthResponse
import cz.cvut.fit.steuejan.travel.api.auth.util.Encryptor
import cz.cvut.fit.steuejan.travel.data.config.DatabaseConfig
import cz.cvut.fit.steuejan.travel.data.model.Credentials
import cz.cvut.fit.steuejan.travel.data.model.EmailLogin
import cz.cvut.fit.steuejan.travel.data.model.Username
import io.ktor.locations.*
import org.joda.time.DateTime

class EmailPasswordController(
    private val daoFactory: DaoFactory,
    private val jwt: JWTController,
    private val encryptor: Encryptor,
    private val validator: Validator,
    private val account: AccountController,
    private val deploymentConfig: DeploymentConfig
) : AuthController<EmailLogin> {

    override suspend fun register(credentials: Credentials<EmailLogin>): AuthResponse {
        val login = credentials.login

        with(validator) {
            validatePassword(login.password)
            validateEmail(login.email, register = true)
            validateUsername(credentials.username)
        }

        val passwordHash = encryptor.hashPassword(login.password)
        val userId = daoFactory.userDao.addUser(
            credentials.username,
            AccountType.EMAIL,
            login.email,
            passwordHash
        )

        val tokens = jwt.createTokens(userId)
        return AuthResponse.success(tokens.accessToken, tokens.refreshToken)
    }

    override suspend fun login(login: EmailLogin): AuthResponse {
        val email = login.email

        with(validator) {
            validateEmail(email, register = false)
            validatePassword(login.password)
        }

        val user = daoFactory.userDao.findByEmail(email, AccountType.EMAIL) ?: throw InvalidLoginException()
        val userPassword = (user.credentials.login as EmailLogin).password

        if (!encryptor.checkPassword(userPassword, login.password)) {
            throw InvalidLoginException()
        }

        val tokens = jwt.createTokens(user.id)
        return AuthResponse.success(tokens.accessToken, tokens.refreshToken)
    }

    @KtorExperimentalLocationsAPI
    suspend fun forgotPassword(email: String, emailSender: EmailSender): Response {
        validator.validateEmail(email, register = false)

        val user = daoFactory.userDao.findByEmail(email, AccountType.EMAIL) ?: return Success(Status.ACCEPTED)

        val expiresAt = DateTime.now().plusMinutes(FORGOT_PASSWORD_EXPIRATION)
        lateinit var randomToken: String

        retryOnError(3) {
            randomToken = getRandomString(DatabaseConfig.FORGOT_PASSWORD_TOKEN_LENGTH)
            val hashedToken = encryptor.hash(randomToken)
            daoFactory.forgotPasswordDao.addForgotPassword(user.id, hashedToken, expiresAt)
        }

        emailSender.sendEmail(email, SUBJECT, getEmailMessage(user.credentials.username, randomToken))

        return Success(Status.ACCEPTED)
    }

    suspend fun resetPassword(token: String, password: ChangePassword): Response {
        val dao = daoFactory.forgotPasswordDao
        val hashedToken = encryptor.hash(token)

        val passwordResetDto = dao.getForgotPassword(hashedToken)
            ?: return Failure(Status.FORBIDDEN, FailureMessages.RESET_PASSWORD_PROHIBITED)

        if (passwordResetDto.expiresAt.isExpired()) {
            dao.deleteForgotPassword(hashedToken)
            return Failure(Status.FORBIDDEN, FailureMessages.RESET_PASSWORD_PROHIBITED)
        }

        return try {
            val response = account.changePassword(passwordResetDto.userId, password, addToDb = false)
            dao.deleteForgotPassword(hashedToken)
            response
        } catch (ex: BadRequestException) {
            Failure(Status.BAD_REQUEST, ex.message)
        }
    }

    @KtorExperimentalLocationsAPI
    private fun getEmailMessage(username: Username, token: String): String {
        val baseUrl = deploymentConfig.baseUrl
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
        private const val FORGOT_PASSWORD_EXPIRATION = 60
        private const val SUBJECT = "Create new password"
    }
}