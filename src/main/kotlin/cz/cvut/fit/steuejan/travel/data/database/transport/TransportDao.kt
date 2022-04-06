package cz.cvut.fit.steuejan.travel.data.database.transport

import cz.cvut.fit.steuejan.travel.data.database.dao.PointOfInterestDao

class TransportDao : PointOfInterestDao<TransportDto> {
    override suspend fun add(tripId: Int, dto: TransportDto) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(poiId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun edit(tripId: Int, dto: TransportDto) {
        TODO("Not yet implemented")
    }

}