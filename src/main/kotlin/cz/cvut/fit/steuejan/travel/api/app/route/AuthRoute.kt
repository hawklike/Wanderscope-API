package cz.cvut.fit.steuejan.travel.api.app.route

import cz.cvut.fit.steuejan.travel.api.account.model.ChangePassword
import cz.cvut.fit.steuejan.travel.api.app.bussines.EmailSender
import cz.cvut.fit.steuejan.travel.api.app.di.factory.AuthControllerFactory
import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.app.extension.receive
import cz.cvut.fit.steuejan.travel.api.app.extension.respond
import cz.cvut.fit.steuejan.travel.api.app.location.Auth
import cz.cvut.fit.steuejan.travel.api.auth.model.AccountType
import cz.cvut.fit.steuejan.travel.api.auth.request.EmailLoginRequest
import cz.cvut.fit.steuejan.travel.api.auth.request.EmailRegistrationRequest
import cz.cvut.fit.steuejan.travel.api.auth.request.ForgotPasswordRequest
import cz.cvut.fit.steuejan.travel.api.auth.request.RefreshTokenRequest
import io.ktor.application.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.locations.post
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.html.*
import org.koin.ktor.ext.inject


@KtorExperimentalLocationsAPI
fun Route.authRoutes() {
    val authFactory: AuthControllerFactory by inject()
    val emailSender: EmailSender by inject()

    post<Auth.Register> {
        when (it.flow) {
            AccountType.EMAIL -> {
                val request = receive<EmailRegistrationRequest>(EmailRegistrationRequest.MISSING_PARAM)
                val emailController = authFactory.emailPasswordController
                respond(emailController.register(request.getCredentials()))
            }
            AccountType.GOOGLE -> {
                //do nothing for now
            }
        }
    }

    post<Auth.Login> {
        when (it.flow) {
            AccountType.EMAIL -> {
                val request = receive<EmailLoginRequest>(EmailLoginRequest.MISSING_PARAM)
                val emailController = authFactory.emailPasswordController
                respond(emailController.login(request.getLogin()))
            }
            AccountType.GOOGLE -> {
                //do nothing for now
            }
        }
    }

    post<Auth.Refresh> {
        val request = receive<RefreshTokenRequest>(RefreshTokenRequest.MISSING_PARAM)
        val refreshTokenController = authFactory.refreshTokenController
        respond(refreshTokenController.refresh(request.refreshToken))
    }

    post<Auth.ForgotPassword.Send> {
        val request = receive<ForgotPasswordRequest>(ForgotPasswordRequest.MISSING_PARAM)
        val emailController = authFactory.emailPasswordController
        respond(emailController.forgotPassword(request.email, emailSender))
    }

    val passwordParam = "password"
    val confirmPasswordParam = "confirmPassword"

    get<Auth.ForgotPassword.Create> {
        call.respondHtml {
            body {
                h2 {
                    +"Reset your password"
                }
                form(
                    action = "${Auth.URL}${Auth.ForgotPassword.URL}${Auth.ForgotPassword.Reset.URL}?token=${it.token}",
                    encType = FormEncType.applicationXWwwFormUrlEncoded,
                    method = FormMethod.post
                ) {
                    label {
                        +"New password:"
                        p {
                            passwordInput(name = passwordParam)
                        }
                    }
                    label {
                        +"Confirm new password:"
                        p {
                            passwordInput(name = confirmPasswordParam)
                        }
                    }
                    p {
                        submitInput { value = "Reset password" }
                    }
                }
            }
        }
    }

    post<Auth.ForgotPassword.Reset> {
        val formParams = call.receiveParameters()

        val password = formParams[passwordParam]
            ?: throw BadRequestException(FailureMessages.RESET_PASSWORD_MISSING_PASS)
        val confirmPassword = formParams[confirmPasswordParam]
            ?: throw BadRequestException(FailureMessages.RESET_PASSWORD_MISSING_COFIRM_PASS)

        val emailController = authFactory.emailPasswordController
        val response = emailController.resetPassword(it.token, ChangePassword(null, password, confirmPassword))

        val responseText = if (response.isSuccess()) {
            "<h2>Password successfully changed!</h2>"
        } else {
            "<h2>Password reset failed - ${response.message}</h2>"
        }
        call.respondText(responseText, ContentType.Text.Html)
    }
}

