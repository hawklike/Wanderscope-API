package cz.cvut.fit.steuejan.travel.api.account.model

data class ChangePassword(
    val oldPassword: String?,
    val newPassword: String,
    val confirmNewPassword: String
)