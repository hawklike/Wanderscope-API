@file:OptIn(KtorExperimentalLocationsAPI::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package cz.cvut.fit.steuejan.travel.api.app.route

import cz.cvut.fit.steuejan.travel.api.account.controller.AccountController
import cz.cvut.fit.steuejan.travel.api.account.model.ChangePassword
import cz.cvut.fit.steuejan.travel.api.app.bussines.EmailSender
import cz.cvut.fit.steuejan.travel.api.app.di.factory.AuthControllerFactory
import cz.cvut.fit.steuejan.travel.api.app.di.factory.ControllerFactory
import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.app.extension.receive
import cz.cvut.fit.steuejan.travel.api.app.extension.respond
import cz.cvut.fit.steuejan.travel.api.app.location.Auth
import cz.cvut.fit.steuejan.travel.api.auth.controller.EmailPasswordController
import cz.cvut.fit.steuejan.travel.api.auth.controller.RefreshTokenController
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

private const val PASSWORD_PARAM = "password"
private const val CONFIRM_PASSWORD_PARAM = "confirmPassword"

fun Routing.authRoutes() {
    val controllerFactory: ControllerFactory by inject()
    val authFactory: AuthControllerFactory by inject()
    val emailSender: EmailSender by inject()

    with(authFactory) {
        register(emailPasswordController)
        login(emailPasswordController)
        refreshToken(refreshTokenController)

        sendForgotPasswordEmail(emailPasswordController, emailSender)
        showForgotPasswordForm()
        resetPassword(emailPasswordController)
    }

    logout(controllerFactory.accountController)
}

private fun Route.register(emailController: EmailPasswordController) {
    post<Auth.Register> {
        when (it.flow) {
            AccountType.EMAIL -> {
                val request = receive<EmailRegistrationRequest>(EmailRegistrationRequest.MISSING_PARAM)
                respond(emailController.register(request.getCredentials()))
            }
            AccountType.GOOGLE -> {
                //do nothing for now
            }
        }
    }
}

private fun Route.login(emailController: EmailPasswordController) {
    post<Auth.Login> {
        when (it.flow) {
            AccountType.EMAIL -> {
                val request = receive<EmailLoginRequest>(EmailLoginRequest.MISSING_PARAM)
                respond(emailController.login(request.getLogin()))
            }
            AccountType.GOOGLE -> {
                //do nothing for now
            }
        }
    }
}

private fun Route.logout(accountController: AccountController) {
    post<Auth.Logout> {
        val request = receive<RefreshTokenRequest>(RefreshTokenRequest.MISSING_PARAM)
        respond(accountController.logout(request.refreshToken))
    }
}

private fun Route.refreshToken(refreshTokenController: RefreshTokenController) {
    post<Auth.Refresh> {
        val request = receive<RefreshTokenRequest>(RefreshTokenRequest.MISSING_PARAM)
        respond(refreshTokenController.refresh(request.refreshToken))
    }
}

private fun Route.sendForgotPasswordEmail(emailController: EmailPasswordController, emailSender: EmailSender) {
    post<Auth.ForgotPassword.Send> {
        val request = receive<ForgotPasswordRequest>(ForgotPasswordRequest.MISSING_PARAM)
        respond(emailController.forgotPassword(request.email, emailSender))
    }
}

private fun Route.showForgotPasswordForm() {
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
                            passwordInput(name = PASSWORD_PARAM)
                        }
                    }
                    label {
                        +"Confirm new password:"
                        p {
                            passwordInput(name = CONFIRM_PASSWORD_PARAM)
                        }
                    }
                    p {
                        submitInput { value = "Reset password" }
                    }
                }
            }
        }
    }
}

private fun Route.resetPassword(emailController: EmailPasswordController) {
    post<Auth.ForgotPassword.Reset> {
        val formParams = call.receiveParameters()

        val password = formParams[PASSWORD_PARAM]
            ?: throw BadRequestException(FailureMessages.RESET_PASSWORD_MISSING_PASS)
        val confirmPassword = formParams[CONFIRM_PASSWORD_PARAM]
            ?: throw BadRequestException(FailureMessages.RESET_PASSWORD_MISSING_COFIRM_PASS)

        val passwordRequest = ChangePassword(null, password, confirmPassword)
        val response = emailController.resetPassword(it.token, passwordRequest)

        val responseText = if (response.isSuccess()) {
            "<h2>Password successfully changed!</h2>"
        } else {
            "<h2>Password reset failed - ${response.message}</h2>"
        }
        call.respondText(responseText, ContentType.Text.Html)
    }
}
