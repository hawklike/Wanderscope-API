package cz.cvut.fit.steuejan.travel.api.app.location

import cz.cvut.fit.steuejan.travel.api.auth.model.AccountType
import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location(Auth.URL)
object Auth {
    @Location(Register.URL)
    class Register(val flow: AccountType) {
        companion object {
            const val URL = "/register"
        }
    }

    @Location(Login.URL)
    class Login(val flow: AccountType) {
        companion object {
            const val URL = "/login"
        }
    }

    @Location(Refresh.URL)
    class Refresh {
        companion object {
            const val URL = "/refresh"
        }
    }

    @Location(ForgotPassword.URL)
    class ForgotPassword {
        @Location(Send.URL)
        class Send {
            companion object {
                const val URL = "/send"
            }
        }

        @Location(Create.URL)
        class Create(val token: String) {
            companion object {
                const val URL = "/create"
            }
        }

        @Location(Reset.URL)
        class Reset(val token: String) {
            companion object {
                const val URL = "/reset"
            }
        }

        companion object {
            const val URL = "/forgotPassword"
        }
    }

    const val URL = "/auth"
}