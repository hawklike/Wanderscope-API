package cz.cvut.fit.steuejan.travel.api.auth.controller

import cz.cvut.fit.steuejan.travel.api.account.model.ChangePassword
import cz.cvut.fit.steuejan.travel.api.app.response.general.Response

interface AuthAccount {
    suspend fun logout(refreshToken: String): Response
    suspend fun logoutAllDevices(userId: Int): Response
    suspend fun changePassword(userId: Int, passwordRequest: ChangePassword, addToDb: Boolean): Response
}