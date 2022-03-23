package cz.cvut.fit.steuejan.travel.data.model

import cz.cvut.fit.steuejan.travel.api.auth.model.AccountType

data class Credentials<T : Login>(val username: Username, val login: T) {
    val accountType: AccountType
        get() = login.accountType
}

sealed class Login(val account: String) {
    abstract fun extractEmail(): String
    abstract val accountType: AccountType
}

class EmailLogin(email: String, val password: String) : Login(email) {
    override fun extractEmail(): String {
        return account
    }

    override val accountType = AccountType.EMAIL
}

class GoogleLogin(email: String) : Login(email.asGoogleAccount()) {
    override fun extractEmail(): String {
        return account.reversed().substringAfter("@").reversed()
    }

    override val accountType = AccountType.GOOGLE

    companion object {
        fun String.asGoogleAccount() = this + EMAIL_POSTFIX
        private const val EMAIL_POSTFIX = "@google"
    }
}