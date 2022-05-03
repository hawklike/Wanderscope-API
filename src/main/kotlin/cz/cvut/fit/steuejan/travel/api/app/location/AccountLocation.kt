@file:OptIn(KtorExperimentalLocationsAPI::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package cz.cvut.fit.steuejan.travel.api.app.location

import io.ktor.locations.*

@Location(Account.URL)
object Account {
    const val URL = "/account"

    @Location(ChangePassword.URL)
    class ChangePassword {
        companion object {
            const val URL = "/changePassword"
        }
    }

    @Location(LogoutAll.URL)
    class LogoutAll {
        companion object {
            const val URL = "/logoutAll"
        }
    }

    @Location(DisplayName.URL)
    class DisplayName {
        companion object {
            const val URL = "/name"
        }
    }
}