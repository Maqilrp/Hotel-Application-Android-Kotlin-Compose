package com.festra.hotelapplication.model

data class Booking(
    val id: Long? = null,
    val userId: String,
    val nama: String,
    val jumlahOrang: String,
    val jenisKamar: String,
    val checkIn: String,
    val checkOut: String,
    val imageId: String,
    val pesan: String
)
