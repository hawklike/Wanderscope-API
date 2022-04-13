package cz.cvut.fit.steuejan.travel.api.app.di

import cz.cvut.fit.steuejan.travel.api.app.config.*
import cz.cvut.fit.steuejan.travel.api.auth.jwt.JWTConfig
import cz.cvut.fit.steuejan.travel.data.config.DatabaseConfig
import cz.cvut.fit.steuejan.travel.data.config.Hikari
import cz.cvut.fit.steuejan.travel.data.config.PostgresHikari
import io.ktor.application.*
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

val Application.configModule: Module
    get() = module {

        fun provideApplicationConfig() = this@configModule.environment.config

        single { provideApplicationConfig() }

        single { JWTConfig(get()) } bind AppConfig::class
        single { DatabaseConfig(get()) } bind AppConfig::class
        single { LimitsConfig(get()) } bind AppConfig::class
        single { EmailConfig(get()) } bind AppConfig::class
        single { DeploymentConfig(get()) } bind AppConfig::class
        single { AmazonS3Config(get()) } bind AppConfig::class

        single<Hikari> { PostgresHikari(get()) }
    }