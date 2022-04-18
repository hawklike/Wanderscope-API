package cz.cvut.fit.steuejan.travel.data.database.expense.dao

import cz.cvut.fit.steuejan.travel.data.database.expense.dto.ExpenseRoomDto

interface ExpenseRoomDao {
    suspend fun createRoom(tripId: Int, room: ExpenseRoomDto): Int
    suspend fun findRoom(tripId: Int, roomId: Int): ExpenseRoomDto?
    suspend fun deleteRoom(tripId: Int, roomId: Int): Boolean
    suspend fun editRoom(tripId: Int, roomId: Int, room: ExpenseRoomDto): Boolean
    suspend fun editName(tripId: Int, roomId: Int, name: String): Boolean
    suspend fun editPersons(tripId: Int, roomId: Int, persons: List<String>): Boolean
    suspend fun getPersons(tripId: Int, roomId: Int): List<String>
}