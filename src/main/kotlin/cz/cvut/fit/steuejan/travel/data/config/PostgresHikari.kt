package cz.cvut.fit.steuejan.travel.data.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

class PostgresHikari(dbConfig: DatabaseConfig) : Hikari(dbConfig) {
    override fun init(): HikariDataSource {
        val config = HikariConfig().apply {
            with(dbConfig) {
                jdbcUrl = "jdbc:postgresql://$host:$port/$name"
                username = user
                password = pass
                isAutoCommit = false
                maximumPoolSize = maxPoolSize
                transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            }
        }
        config.validate()
        return HikariDataSource(config)
    }
}