package com.festra.hotelapplication.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.festra.hotelapplication.model.Booking

@Dao
interface BookingDao {

    @Insert
    suspend fun insert(booking: Booking)

    @Update
    suspend fun update(booking: Booking)

    @Query("Select * from booking order by id desc")
    fun getBooking(): kotlinx.coroutines.flow.Flow<List<Booking>>

    @Query("Select * from booking where id = :id")
    suspend fun getBookingById(id: Long): Booking?

    @Query("Delete from booking where id = :id")
    suspend fun deleteById(id: Long)
}