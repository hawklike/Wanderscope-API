package cz.cvut.fit.steuejan.travel.data.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import cz.cvut.fit.steuejan.travel.data.util.PostgresUriParser

class PostgresHikari(dbConfig: DatabaseConfig) : Hikari(dbConfig) {
    override fun init(): HikariDataSource {
        val jdbcConnection = PostgresUriParser().getConnection(dbConfig.databaseUri)

        val config = HikariConfig().apply {
            jdbcUrl = jdbcConnection.dbUrl
            username = jdbcConnection.username
            password = jdbcConnection.password
            isAutoCommit = false
            maximumPoolSize = dbConfig.maxPoolSize
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            addDataSourceProperty("rewriteBatchedInserts", true.toString())
        }
        config.validate()
        return HikariDataSource(config)
    }
}