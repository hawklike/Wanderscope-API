package cz.cvut.fit.steuejan.travel.api.auth.exception

open class AuthorizationException(override val message: String, val code: Int) : Exception(message)

class InvalidRefreshTokenException(
    message: String = "Refresh token is invalid.",
    code: Int = 1024
) : AuthorizationException(message, code)

class RefreshTokenExpiredException(
    message: String = "Refresh token has expired.",
    code: Int = 2048
) : AuthorizationException(message, code)

class PasswordChangeProhibitedException(
    message: String = "Given password is wrong.",
    code: Int = 4096
) : AuthorizationException(message, code)