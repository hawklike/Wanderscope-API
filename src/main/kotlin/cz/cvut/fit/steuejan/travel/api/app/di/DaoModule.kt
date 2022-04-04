package cz.cvut.fit.steuejan.travel.api.app.di

import cz.cvut.fit.steuejan.travel.api.app.di.factory.DaoFactory
import cz.cvut.fit.steuejan.travel.data.database.forgotpassword.dao.ForgotPasswordDao
import cz.cvut.fit.steuejan.travel.data.database.forgotpassword.dao.ForgotPasswordDaoImpl
import cz.cvut.fit.steuejan.travel.data.database.token.dao.TokenDao
import cz.cvut.fit.steuejan.travel.data.database.token.dao.TokenDaoImp
import cz.cvut.fit.steuejan.travel.data.database.user.dao.UserDao
import cz.cvut.fit.steuejan.travel.data.database.user.dao.UserDaoImpl
import org.koin.dsl.module

val daoModule = module {
    single<UserDao> { UserDaoImpl() }
    single<TokenDao> { TokenDaoImp() }
    single<ForgotPasswordDao> { ForgotPasswordDaoImpl() }

    single { DaoFactory(get(), get(), get()) }
}