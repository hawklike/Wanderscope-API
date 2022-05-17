package cz.cvut.fit.steuejan.travel.api.trip.poi.controller

import cz.cvut.fit.steuejan.travel.api.app.di.factory.DaoFactory
import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.NotFoundException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.app.response.CreatedResponse
import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.app.response.Status
import cz.cvut.fit.steuejan.travel.api.app.response.Success
import cz.cvut.fit.steuejan.travel.api.trip.controller.AbstractTripController
import cz.cvut.fit.steuejan.travel.api.trip.document.model.DocumentOverview
import cz.cvut.fit.steuejan.travel.api.trip.document.response.DocumentOverviewListResponse
import cz.cvut.fit.steuejan.travel.data.config.DatabaseConfig
import cz.cvut.fit.steuejan.travel.data.database.dao.PointOfInterestDao
import cz.cvut.fit.steuejan.travel.data.dto.PointOfInterestDto
import cz.cvut.fit.steuejan.travel.data.model.PointOfInterestType
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Serializable
data class WikiSearch(
    val query: Query
)

@Serializable
data class Query(
    val search: List<Search>
)

@Serializable
data class Search(
    val title: String,
    val pageid: Int
)

@Serializable
data class WikiArticle(
    val query: WikiArticleQuery
)

@Serializable
data class WikiArticleQuery(
    val pages: Map<Int, WikiArticleExtract>
)

@Serializable
data class WikiArticleExtract(
    val extract: String
)

data class WikiSearchBundle(
    val scope: CoroutineScope,
    val searchTerm: String
)

abstract class PointOfInterestController<T : PointOfInterestDto>(
    daoFactory: DaoFactory,
    private val client: HttpClient,
    protected val dao: PointOfInterestDao<T>
) : AbstractTripController(daoFactory) {

    abstract val notFound: String
    abstract val type: PointOfInterestType

    suspend fun add(userId: Int, tripId: Int, dto: T, wiki: WikiSearchBundle? = null): Response {
        val poiId = upsert(userId, tripId, dto) {
            dao.add(tripId, dto)
        }
        wiki?.let { findWikipediaArticle(it) }
        return CreatedResponse.success(poiId)
    }

    private fun findWikipediaArticle(wiki: WikiSearchBundle) {
        if (type != PointOfInterestType.PLACE) return

        val enUrl = "https://en.wikipedia.org/w/api.php"
        val csUrl = "https://cs.wikipedia.org/w/api.php"
        val enExtractUrl = "https://en.wikipedia.org/api/rest_v1/page/summary"
        val csExtractUrl = "https://cs.wikipedia.org/api/rest_v1/page/summary"

        wiki.scope.launch(Dispatchers.IO) {
            val titleRequestEn = async { getPageId(wiki.searchTerm, enUrl) }
            val titleRequestCs = async { getPageId(wiki.searchTerm, csUrl) }

            val titleEn = titleRequestEn.await()
            val titleCs = titleRequestCs.await()

            val extractRequestEn = async { getExtract(titleEn, enExtractUrl) }
            val extractRequestCs = async { getExtract(titleCs, csExtractUrl) }

            val extractEn = extractRequestEn.await()
            val extractCs = extractRequestCs.await()

            extractEn
            extractCs
        }
    }

    private suspend fun getExtract(title: String?, url: String): String? {
        title ?: return null
        val encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8)
        return kotlin.runCatching {
            client.get<WikiArticleExtract>("$url/$encodedTitle") {
            }.extract
        }.getOrNull()
    }

    private suspend fun getPageId(searchTerm: String, url: String): String? {
        return kotlin.runCatching {
            client.get<WikiSearch>(url) {
                wikiSearchParams()
                parameter("srsearch", searchTerm)
            }.query.search.firstOrNull()?.title
        }.getOrNull()
    }

    private fun HttpRequestBuilder.wikiSearchParams() {
        parameter("format", "json")
        parameter("action", "query")
        parameter("list", "search")
        parameter("srprop", "sectiontitle")
        parameter("srlimit", 1)
    }

    suspend fun get(userId: Int, tripId: Int, poiId: Int): Response {
        return viewOrThrow(userId, tripId) {
            dao.find(tripId, poiId)?.toResponse()
                ?: throw NotFoundException(notFound)
        }
    }

    suspend fun delete(userId: Int, tripId: Int, poiId: Int): Response {
        editOrThrow(userId, tripId) {
            if (!dao.delete(tripId, poiId)) {
                throw NotFoundException(notFound)
            }
        }
        return Success(Status.NO_CONTENT)
    }

    suspend fun edit(userId: Int, tripId: Int, poiId: Int, dto: T): Response {
        upsert(userId, tripId, dto) {
            if (!dao.edit(tripId, poiId, dto)) {
                throw NotFoundException(notFound)
            }
        }
        return Success(Status.NO_CONTENT)
    }

    open suspend fun showInTrip(userId: Int, tripId: Int): List<T> {
        viewOrThrow(userId, tripId)
        return dao.show(tripId)
    }

    suspend fun showDocuments(userId: Int, tripId: Int, poiId: Int): Response {
        viewOrThrow(userId, tripId)
        val documents = daoFactory.documentDao.getDocuments(tripId, poiId, this.type)
        return DocumentOverviewListResponse.success(documents.map(DocumentOverview::fromDto))
    }

    private suspend fun <R> upsert(userId: Int, tripId: Int, dto: T, call: suspend () -> R): R {
        if (dto.name.length > DatabaseConfig.NAME_LENGTH) {
            throw BadRequestException(FailureMessages.NAME_TOO_LONG)
        }
        return editOrThrow(userId, tripId) {
            call.invoke()
        }
    }
}