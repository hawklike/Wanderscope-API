package cz.cvut.fit.steuejan.travel.api.app.bussines

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.GetObjectRequest
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.smithy.kotlin.runtime.content.ByteStream
import aws.smithy.kotlin.runtime.content.toByteArray
import cz.cvut.fit.steuejan.travel.api.trip.document.model.FileWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AmazonS3 {

    suspend fun uploadFile(id: String, filename: String, file: FileWrapper) = withContext(Dispatchers.IO) {
        val fileMetadata = mapOf("name" to filename)

        val request = PutObjectRequest {
            bucket = "wanderscope-dev"
            key = id
            metadata = fileMetadata
            body = ByteStream.fromBytes(file.rawData)
        }

        val response = S3Client { region = "eu-central-1" }.use { s3 ->
            s3.putObject(request)
        }

        response.eTag != null
    }

    suspend fun downloadFile(id: String) = withContext(Dispatchers.IO) {
        val request = GetObjectRequest {
            bucket = "wanderscope-dev"
            key = id
        }

        S3Client { region = "eu-central-1" }.use { s3 ->
            s3.getObject(request) { response ->
                val name = response.metadata?.get("name")
                val bytes = response.body?.toByteArray()

                if (name == null || bytes == null) {
                    null
                } else {
                    FileWrapper(name, bytes)
                }
            }
        }
    }
}