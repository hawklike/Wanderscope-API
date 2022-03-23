package cz.cvut.fit.steuejan.travel.api.app.di.factory

import cz.cvut.fit.steuejan.travel.api.auth.controller.EmailPasswordController
import cz.cvut.fit.steuejan.travel.api.auth.controller.RefreshTokenController

class AuthControllerFactory(
    val emailPasswordController: EmailPasswordController,
    val refreshTokenController: RefreshTokenController
)