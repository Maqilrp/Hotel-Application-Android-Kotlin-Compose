import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.festra.hotelapplication.model.Booking

@Dao
interface BookingDao {
    @Insert
    suspend fun insertBooking(booking: Booking): Long

    @Query("SELECT * FROM bookings WHERE userId = :userId")
    suspend fun getBookingsByUserId(userId: String): List<Booking>

    @Query("DELETE FROM bookings WHERE id = :id")
    suspend fun deleteBookingById(id: Long)
}
