package cz.cvut.fit.steuejan.travel.api.app.exception

class NotFoundException(override val message: String = "Resource not found") : Exception()