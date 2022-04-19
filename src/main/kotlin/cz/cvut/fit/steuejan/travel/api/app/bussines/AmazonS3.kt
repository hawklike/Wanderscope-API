package cz.cvut.fit.steuejan.travel.api.app.bussines

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.*
import aws.smithy.kotlin.runtime.content.ByteStream
import aws.smithy.kotlin.runtime.content.toByteArray
import cz.cvut.fit.steuejan.travel.api.app.config.AmazonS3Config
import cz.cvut.fit.steuejan.travel.api.trip.document.model.FileWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AmazonS3(private val config: AmazonS3Config) {

    suspend fun uploadFile(id: String, filename: String, file: FileWrapper) = withContext(Dispatchers.IO) {
        val fileMetadata = mapOf(NAME to filename)

        val request = PutObjectRequest {
            bucket = config.bucket
            key = id
            metadata = fileMetadata
            body = ByteStream.fromBytes(file.rawData)
        }

        val response = S3Client { region = config.region }.use { s3 ->
            s3.putObject(request)
        }

        response.eTag != null
    }

    suspend fun downloadFile(id: String) = withContext(Dispatchers.IO) {
        val request = GetObjectRequest {
            bucket = config.bucket
            key = id
        }

        S3Client { region = config.region }.use { s3 ->
            s3.getObject(request) { response ->
                val name = response.metadata?.get(NAME)
                val bytes = response.body?.toByteArray()

                if (name == null || bytes == null) {
                    null
                } else {
                    FileWrapper(name, bytes)
                }
            }
        }
    }

    suspend fun deleteFile(id: String) = withContext(Dispatchers.IO) {
        val objectId = ObjectIdentifier {
            key = id
        }

        val request = DeleteObjectsRequest {
            bucket = config.bucket
            delete = Delete {
                objects = listOf(objectId)
            }
        }

        val response = S3Client { region = config.region }.use { s3 ->
            s3.deleteObjects(request)
        }

        response.deleted != null
    }

    companion object {
        const val NAME = "name"
    }
}