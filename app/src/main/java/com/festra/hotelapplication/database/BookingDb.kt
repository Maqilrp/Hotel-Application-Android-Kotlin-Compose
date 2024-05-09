package com.festra.hotelapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.festra.hotelapplication.model.Booking

@Database(entities = [Booking::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class BookingDb: RoomDatabase() {

    abstract val dao: BookingDao

    companion object{
        @Volatile
        private var INSTANCE: BookingDb? = null

        fun getInstance(context: Context): BookingDb{
            synchronized(this){
                var instance = INSTANCE

                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        BookingDb::class.java,
                        "booking.db"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}