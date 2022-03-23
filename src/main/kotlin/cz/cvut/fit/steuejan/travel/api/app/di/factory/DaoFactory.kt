package cz.cvut.fit.steuejan.travel.api.app.di.factory

import cz.cvut.fit.steuejan.travel.data.dao.forgotpassword.ForgotPasswordDao
import cz.cvut.fit.steuejan.travel.data.dao.token.TokenDao
import cz.cvut.fit.steuejan.travel.data.dao.user.UserDao

class DaoFactory(
    val userDao: UserDao,
    val tokenDao: TokenDao,
    val forgotPasswordDao: ForgotPasswordDao
)