package cz.cvut.fit.steuejan.travel.api.auth.controller

import cz.cvut.fit.steuejan.travel.api.account.model.ChangePassword
import cz.cvut.fit.steuejan.travel.api.app.response.general.Response
import cz.cvut.fit.steuejan.travel.data.model.Username

interface AuthAccount {
    fun logout(refreshToken: String): Response
    fun logoutAllDevices(username: Username): Response
    fun changePassword(username: Username, password: ChangePassword): Response
}