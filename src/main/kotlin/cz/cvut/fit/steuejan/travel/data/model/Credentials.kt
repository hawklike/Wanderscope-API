package cz.cvut.fit.steuejan.travel.data.model

import cz.cvut.fit.steuejan.travel.api.auth.model.AccountType

data class Credentials<out T : Login>(val username: Username, val login: T) {
    val accountType: AccountType
        get() = login.accountType
}

sealed class Login(val email: String) {
    abstract val accountType: AccountType
}

class EmailLogin(email: String, val password: String) : Login(email) {
    override val accountType = AccountType.EMAIL
}

class GoogleLogin(email: String) : Login(email) {
    override val accountType = AccountType.GOOGLE
}