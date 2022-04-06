package cz.cvut.fit.steuejan.travel.api.trip.points.transport.controller

import cz.cvut.fit.steuejan.travel.api.app.di.factory.DaoFactory
import cz.cvut.fit.steuejan.travel.api.trip.points.controller.AbstractPointOfInterestController
import cz.cvut.fit.steuejan.travel.data.database.transport.TransportDto

class TransportController(
    daoFactory: DaoFactory
) : AbstractPointOfInterestController<TransportDto>(daoFactory, daoFactory.transportDao)