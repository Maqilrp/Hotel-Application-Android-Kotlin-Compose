package com.festra.hotelapplication.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festra.hotelapplication.database.BookingDao
import com.festra.hotelapplication.model.Booking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class DetailBookingViewModel(private val dao: BookingDao): ViewModel() {

    // untuk insert data
    fun insert(
        nama:String,
        jumlahOrang:String,
        jenisKamar:String,
        checkIn: Date,
        checkOut: Date,
        pesan: String
    ) {
        val booking = Booking(
            nama = nama,
            jumlahOrang = jumlahOrang,
            jenisKamar = jenisKamar,
            checkIn = checkIn,
            checkOut = checkOut,
            pesan = pesan
        )
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(booking)
        }
    }

    // untuk mengambil data
    suspend fun getBooking(id: Long): Booking? {
        return dao.getBookingById(id)
    }
    // untuk mengupdate data
    fun update(
        id: Long,
        nama:String,
        jumlahOrang:String,
        jenisKamar:String,
        checkIn: Date,
        checkOut: Date,
        pesan: String
    ){
        val booking = Booking(
            id = id,
            nama = nama,
            jumlahOrang = jumlahOrang,
            jenisKamar = jenisKamar,
            checkIn = checkIn,
            checkOut = checkOut,
            pesan = pesan
        )
        viewModelScope.launch(Dispatchers.IO) {
            dao.update(booking)
        }
    }

    // untuk delete data
    fun delete(id: Long){
        viewModelScope.launch(Dispatchers.IO){
            dao.deleteById(id)
        }
    }

}