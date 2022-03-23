package cz.cvut.fit.steuejan.travel.api.app.response.general

interface Response {
    val status: Status
    val message: String
    val code: Int

    fun isSuccess(): Boolean = status in listOf(Status.SUCCESS, Status.ACCEPTED, Status.NO_CONTENT)
}