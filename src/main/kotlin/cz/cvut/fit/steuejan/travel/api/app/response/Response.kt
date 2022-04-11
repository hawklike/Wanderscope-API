package cz.cvut.fit.steuejan.travel.api.app.response

interface Response {
    val status: Status
    val message: String
    val code: Int

    fun isSuccess(): Boolean = status.isSuccess()
}