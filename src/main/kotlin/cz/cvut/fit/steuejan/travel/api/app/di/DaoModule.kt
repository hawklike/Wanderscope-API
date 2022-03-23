package cz.cvut.fit.steuejan.travel.api.app.di

import cz.cvut.fit.steuejan.travel.api.app.di.factory.DaoFactory
import cz.cvut.fit.steuejan.travel.data.dao.forgotpassword.ForgotPasswordDao
import cz.cvut.fit.steuejan.travel.data.dao.forgotpassword.ForgotPasswordDaoImpl
import cz.cvut.fit.steuejan.travel.data.dao.token.TokenDao
import cz.cvut.fit.steuejan.travel.data.dao.token.TokenDaoImp
import cz.cvut.fit.steuejan.travel.data.dao.user.UserDao
import cz.cvut.fit.steuejan.travel.data.dao.user.UserDaoImpl
import cz.cvut.fit.steuejan.travel.data.dao.user.UserDaoWithoutDbs
import cz.cvut.fit.steuejan.travel.data.database.InMemoryDatabase
import org.koin.core.qualifier.named
import org.koin.dsl.module

val daoModule = module {
    single { InMemoryDatabase() }

    single<UserDao>(named("prototype")) { UserDaoWithoutDbs(get()) }
    single<UserDao> { UserDaoImpl() }
    single<TokenDao> { TokenDaoImp(get()) }
    single<ForgotPasswordDao> { ForgotPasswordDaoImpl(get()) }

    single { DaoFactory(get(qualifier = named("prototype")), get(), get()) }
}