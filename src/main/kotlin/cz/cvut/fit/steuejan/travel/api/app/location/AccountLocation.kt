package cz.cvut.fit.steuejan.travel.api.app.location

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location(Account.URL)
object Account {
    @Location(ChangePassword.URL)
    class ChangePassword {
        companion object {
            const val URL = "/changePassword"
        }
    }

    @Location(Logout.URL)
    class Logout {
        companion object {
            const val URL = "/logout"
        }
    }

    @Location(LogoutAll.URL)
    class LogoutAll {
        companion object {
            const val URL = "/logoutAll"
        }
    }

    const val URL = "/account"
}