package cz.cvut.fit.steuejan.travel.api.auth.exception

open class AuthenticationException(override val message: String, val code: Int) : Exception(message)

class EmailAlreadyExistsException(
    message: String = "Account with this email already exists.",
    code: Int = 128
) : AuthenticationException(message, code)

class UsernameAlreadyExistsException(
    message: String = "This username already exists.",
    code: Int = 256
) : AuthenticationException(message, code)

class InvalidLoginException(
    message: String = "Email or password is incorrect.",
    code: Int = 512
) : AuthenticationException(message, code)