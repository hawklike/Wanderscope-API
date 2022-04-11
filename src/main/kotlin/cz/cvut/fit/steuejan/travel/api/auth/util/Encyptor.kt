package cz.cvut.fit.steuejan.travel.api.auth.util

import cz.cvut.fit.steuejan.travel.api.app.config.AppConfig
import io.ktor.util.*
import org.mindrot.jbcrypt.BCrypt
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

interface Encryptor {
    fun hashPassword(password: String): String
    fun hash(data: String): String

    /**
     * @return true if [plaintext] matches with the [passwordHash]
     */
    fun checkPassword(passwordHash: String?, plaintext: String): Boolean
}

class ApiEncryptor(secret: String) : Encryptor {

    constructor(config: AppConfig) : this(config.config.property("hmac.key").getString())

    private val hmacKey: SecretKeySpec = SecretKeySpec(secret.toByteArray(), TOKEN_ALGORITHM)

    override fun hashPassword(password: String): String {
        if (password.length > MAX_PASSWORD_LENGTH) {
            throw IllegalArgumentException("password is too long, may be max $MAX_PASSWORD_LENGTH characters")
        }
        return BCrypt.hashpw(password, BCrypt.gensalt(BCRYPT_SALT_ROUNDS))
    }

    override fun hash(data: String): String {
        val hmac = Mac.getInstance(TOKEN_ALGORITHM)
        hmac.init(hmacKey)
        return hex(hmac.doFinal(data.toByteArray(Charsets.UTF_8)))
    }

    override fun checkPassword(passwordHash: String?, plaintext: String): Boolean {
        return if (passwordHash == null) {
            false
        } else {
            BCrypt.checkpw(plaintext, passwordHash)
        }
    }

    companion object {
        private const val TOKEN_ALGORITHM = "HmacSHA256"
        private const val BCRYPT_SALT_ROUNDS = 12
        private const val MAX_PASSWORD_LENGTH = 50
    }
}