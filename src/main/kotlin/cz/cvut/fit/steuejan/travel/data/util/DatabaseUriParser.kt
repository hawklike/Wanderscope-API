package cz.cvut.fit.steuejan.travel.data.util

import java.net.URI

interface JdbcConnection {
    val username: String
    val password: String
    val dbUrl: String
}

data class PostgresConnection(
    override val username: String,
    override val password: String,
    override val dbUrl: String
) : JdbcConnection

interface DatabaseUriParser {
    fun getConnection(dbUri: String): JdbcConnection
}

class PostgresUriParser : DatabaseUriParser {
    /**
     * @param dbUri provide in the following format: postgres://username:password@host/dbname
     */
    override fun getConnection(dbUri: String): JdbcConnection {
        val uri = URI(dbUri)
        val username = uri.userInfo.split(":")[0]
        val password = uri.userInfo.split(":")[1]
        val dbUrl = "jdbc:postgresql://${uri.host}:${uri.port}${uri.path}"
        return PostgresConnection(username, password, dbUrl)
    }
}