package cz.cvut.fit.steuejan.travel.api.app.di

import cz.cvut.fit.steuejan.travel.api.app.di.factory.DaoFactory
import cz.cvut.fit.steuejan.travel.data.database.accomodation.AccomodationDao
import cz.cvut.fit.steuejan.travel.data.database.activity.ActivityDao
import cz.cvut.fit.steuejan.travel.data.database.document.dao.DocumentDao
import cz.cvut.fit.steuejan.travel.data.database.document.dao.DocumentDaoImpl
import cz.cvut.fit.steuejan.travel.data.database.forgotpassword.dao.ForgotPasswordDao
import cz.cvut.fit.steuejan.travel.data.database.forgotpassword.dao.ForgotPasswordDaoImpl
import cz.cvut.fit.steuejan.travel.data.database.place.PlaceDao
import cz.cvut.fit.steuejan.travel.data.database.token.dao.TokenDao
import cz.cvut.fit.steuejan.travel.data.database.token.dao.TokenDaoImp
import cz.cvut.fit.steuejan.travel.data.database.transport.TransportDao
import cz.cvut.fit.steuejan.travel.data.database.trip.dao.TripDao
import cz.cvut.fit.steuejan.travel.data.database.trip.dao.TripDaoImpl
import cz.cvut.fit.steuejan.travel.data.database.tripuser.dao.TripUserDao
import cz.cvut.fit.steuejan.travel.data.database.tripuser.dao.TripUserDaoImpl
import cz.cvut.fit.steuejan.travel.data.database.user.dao.UserDao
import cz.cvut.fit.steuejan.travel.data.database.user.dao.UserDaoImpl
import org.koin.dsl.module

val daoModule = module {
    single<UserDao> { UserDaoImpl() }
    single<TokenDao> { TokenDaoImp() }
    single<ForgotPasswordDao> { ForgotPasswordDaoImpl() }
    single<TripDao> { TripDaoImpl() }
    single<TripUserDao> { TripUserDaoImpl() }

    single { TransportDao() }
    single { AccomodationDao() }
    single { ActivityDao() }
    single { PlaceDao() }
    single<DocumentDao> { DocumentDaoImpl() }

    single { DaoFactory(get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
}