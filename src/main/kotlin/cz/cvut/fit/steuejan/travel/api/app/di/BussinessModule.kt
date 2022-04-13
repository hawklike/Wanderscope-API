package cz.cvut.fit.steuejan.travel.api.app.di

import cz.cvut.fit.steuejan.travel.api.app.bussines.AmazonS3
import cz.cvut.fit.steuejan.travel.api.app.bussines.EmailSender
import cz.cvut.fit.steuejan.travel.api.app.bussines.Validator
import org.koin.dsl.module

val bussinessModule = module {
    factory { EmailSender(get()) }
    single { Validator(get(), get()) }
    single { AmazonS3(get()) }
}