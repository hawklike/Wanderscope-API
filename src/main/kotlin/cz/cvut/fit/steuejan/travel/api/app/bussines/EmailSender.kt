package cz.cvut.fit.steuejan.travel.api.app.bussines

import cz.cvut.fit.steuejan.travel.api.app.config.EmailConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.commons.mail.SimpleEmail

class EmailSender(
    private val hostName: String,
    private val port: Int,
    private val credentials: Authenticator
) {
    constructor(config: EmailConfig) : this(
        config.host,
        config.port,
        Authenticator(config.account, config.password)
    )

    val email: SimpleEmail
        get() = SimpleEmail().apply {
            hostName = this@EmailSender.hostName
            setSmtpPort(port)
            setAuthentication(credentials.emailAccount, credentials.password)
            isSSLOnConnect = true
        }

    suspend fun sendEmailAsync(
        receiver: String,
        subject: String,
        message: String,
        sender: String = credentials.emailAccount
    ) = withContext(Dispatchers.IO) {
        launch() {
            email.apply {
                setFrom(sender)
                setSubject(subject)
                setMsg(message)
                addTo(receiver)
            }.send()
        }
    }

    class Authenticator(val emailAccount: String, val password: String)
}
