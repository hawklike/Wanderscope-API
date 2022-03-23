package cz.cvut.fit.steuejan.travel.data.config

import com.zaxxer.hikari.HikariDataSource

abstract class Hikari(val dbConfig: DatabaseConfig) {
    abstract fun init(): HikariDataSource
}