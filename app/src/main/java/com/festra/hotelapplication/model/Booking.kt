package com.festra.hotelapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey

//@Entity(tableName = "booking")
//data class Booking (
//    @PrimaryKey(autoGenerate = true)
//    val id: Long = 0L,
//    val nama: String,
//    val jumlahOrang: String,
//    val jenisKamar: String,
//    val checkIn: Date,
//    val checkOut: Date,
//    val pesan: String
//)

@Entity(tableName = "booking")
data class Booking(
    @PrimaryKey
    val id: Long,
    val nama: String,
    val jumlah_orang: String,
    val jenis_kamar: String,
    val check_in: String,
    val check_out: String,
    val imageId: String,
    val pesan: String
)