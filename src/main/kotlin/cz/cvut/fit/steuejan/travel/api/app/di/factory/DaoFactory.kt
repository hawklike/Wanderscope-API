package cz.cvut.fit.steuejan.travel.api.app.di.factory

import cz.cvut.fit.steuejan.travel.data.database.forgotpassword.dao.ForgotPasswordDao
import cz.cvut.fit.steuejan.travel.data.database.token.dao.TokenDao
import cz.cvut.fit.steuejan.travel.data.database.transport.TransportDao
import cz.cvut.fit.steuejan.travel.data.database.trip.dao.TripDao
import cz.cvut.fit.steuejan.travel.data.database.tripuser.dao.TripUserDao
import cz.cvut.fit.steuejan.travel.data.database.user.dao.UserDao

class DaoFactory(
    val userDao: UserDao,
    val tokenDao: TokenDao,
    val forgotPasswordDao: ForgotPasswordDao,
    val tripDao: TripDao,
    val tripUserDao: TripUserDao,
    val transportDao: TransportDao
)