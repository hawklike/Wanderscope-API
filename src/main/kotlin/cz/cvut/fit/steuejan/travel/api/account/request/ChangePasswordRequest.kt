package cz.cvut.fit.steuejan.travel.api.account.request

import cz.cvut.fit.steuejan.travel.api.account.model.ChangePassword
import cz.cvut.fit.steuejan.travel.api.app.request.Request
import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordRequest(
    val oldPassword: String,
    val newPassword: String,
    val confirmNewPassword: String
) : Request {
    fun toChangePassword(): ChangePassword = ChangePassword(oldPassword, newPassword, confirmNewPassword)

    companion object {
        const val MISSING_PARAM =
            "Required 'oldPassword': String, 'newPassword': String and 'confirmNewPassword': String."
    }
}
