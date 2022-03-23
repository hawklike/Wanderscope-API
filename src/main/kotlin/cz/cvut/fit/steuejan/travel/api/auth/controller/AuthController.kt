package cz.cvut.fit.steuejan.travel.api.auth.controller

import cz.cvut.fit.steuejan.travel.api.app.response.general.Response
import cz.cvut.fit.steuejan.travel.data.model.Credentials
import cz.cvut.fit.steuejan.travel.data.model.Login

interface AuthController<T : Login> {
    fun register(credentials: Credentials<T>): Response
    fun login(login: T): Response
}