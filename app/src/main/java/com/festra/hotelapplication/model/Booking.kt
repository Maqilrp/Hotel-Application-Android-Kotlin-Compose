package com.festra.hotelapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "booking")
data class Booking (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nama: String,
    val jumlahOrang: String,
    val jenisKamar: String,
    val checkIn: Date,
    val checkOut: Date,
    val pesan: String
)

