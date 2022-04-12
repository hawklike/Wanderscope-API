package cz.cvut.fit.steuejan.travel.api.auth.exception

import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages

open class AuthenticationException(override val message: String, val code: Int) : Exception(message)

class EmailAlreadyExistsException(
    message: String = FailureMessages.EMAIL_ALREADY_EXISTS,
    code: Int = 1
) : AuthenticationException(message, code)

class UsernameAlreadyExistsException(
    message: String = FailureMessages.USERNAME_ALREADY_EXISTS,
    code: Int = 2
) : AuthenticationException(message, code)

class InvalidLoginException(
    message: String = FailureMessages.EMAIL_PASSWORD_INCORRECT,
    code: Int = 3
) : AuthenticationException(message, code)