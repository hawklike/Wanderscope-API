package cz.cvut.fit.steuejan.travel.api.auth.exception

import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages

open class AuthorizationException(override val message: String, val code: Int) : Exception(message)

class InvalidRefreshTokenException(
    message: String = FailureMessages.REFRESH_TOKEN_INVALID,
    code: Int = 1024
) : AuthorizationException(message, code)

class RefreshTokenExpiredException(
    message: String = FailureMessages.REFRESH_TOKEN_EXPIRED,
    code: Int = 2048
) : AuthorizationException(message, code)

class PasswordChangeProhibitedException(
    message: String = FailureMessages.OLD_PASSWORD_WRONG,
    code: Int = 1
) : AuthorizationException(message, code)