package cz.cvut.fit.steuejan.travel.api.app.config

import io.ktor.config.*

class AmazonS3Config(override val config: ApplicationConfig) : AppConfig {
    val bucket = config.property(BUCKET).getString()
    val region = config.property(REGION).getString()

    companion object {
        private const val BUCKET = "aws.s3.bucket"
        private const val REGION = "aws.s3.region"
    }
}